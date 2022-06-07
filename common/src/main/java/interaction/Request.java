package interaction;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class Request<T> implements Serializable {

    private static final long serialVersionUID = -3750791637366915264L;
    String command;
    String params;
    int Authorization;
    @Setter
    T body;

    public Request(String command){
        this.command = command;
        this.params = null;
        this.body = null;
    }

    public Request(String command, String params){
        this.command = command;
        this.params = params;
        this.body = null;
    }

    public Request(String command, String params, T body){
        this.command = command;
        this.params = params;
        this.body = body;
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
