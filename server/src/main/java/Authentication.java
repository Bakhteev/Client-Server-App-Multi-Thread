import dao.UsersDao;
import dto.UserDto;
import models.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authentication {

    private final UsersDao usersDao;

    public Authentication(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    private char randomChar() {
        int rnd = (int) (Math.random() * 52);
        char base = (rnd < 26) ? 'A' : 'a';
        return (char) (base + rnd % 26);
    }

    private String generateRandomSalt() {
        int randomLenght = (int) (Math.random() * 25);
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < randomLenght; i++) {
            salt.append(randomChar());
        }
        return salt.toString();
    }

    public static String generateHashedPassword(String password) {

        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

    public boolean login(UserDto dto) {
        User user = usersDao.getByLogin(dto.getLogin());
        if (user == null) {
            throw new IllegalArgumentException("You have not registered yet");
//            return false;
        }

        if (comparePasswords(dto.getPassword(), user)) {
            return true;
        } else {
            throw new IllegalArgumentException("Wrong password or login");
        }
    }

    public boolean registration(UserDto dto){
        User user = usersDao.getByLogin(dto.getLogin());
        if(user != null){
            throw new IllegalArgumentException("Login has already registered");
        }
        String prefix = generateRandomSalt();
        String suffix = generateRandomSalt();
        String hashedPassword = generateHashedPassword(prefix + dto.getPassword() + suffix);
        usersDao.create(dto.getLogin(), hashedPassword,prefix,suffix);
        return true;
    }

    private boolean comparePasswords(String password, User user) {
        String passwordWithSalt = user.getPrefix() + password + user.getSuffix();
        return user.getHashedPassword().equals(generateHashedPassword(passwordWithSalt));
    }


}
