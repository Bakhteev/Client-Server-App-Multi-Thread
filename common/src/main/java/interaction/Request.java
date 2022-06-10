package interaction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class Request<T> implements Serializable {

    private static final long serialVersionUID = -3750791637366915264L;
    String command;
    String params;
    Integer Authorization;
    @Setter
    T body;

    public Request(String command, Integer auth){
        this.command = command;
        this.params = null;
        this.body = null;
        this.Authorization = auth;
    }

    public Request(String command, String params, Integer auth){
        this.command = command;
        this.params = params;
        this.body = null;
        this.Authorization = auth;
    }

    public Request(String command, String params, T body, Integer auth){
        this.command = command;
        this.params = params;
        this.body = body;
        this.Authorization = auth;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", params='" + params + '\'' +
                ", body=" + body +
                '}';
    }
}
