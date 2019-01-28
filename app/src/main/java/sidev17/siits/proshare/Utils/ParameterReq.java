package sidev17.siits.proshare.Utils;

class ParameterReq {
    String key;
    String val;

    public ParameterReq(){}

    public ParameterReq(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
