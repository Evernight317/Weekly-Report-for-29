package Ex.ATM_System;

/**
 * Account 创建对象，保存用户的账户信息
 */
public class Account {
    private String cardId;
    private String userName;
    private String sex;
    private String passWord;
    private double money;
    private double moneyLimit;
    
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public double getMoneyLimit() {
        return moneyLimit;
    }
    public void setMoneyLimit(double moneyLimit) {
        this.moneyLimit = moneyLimit;
    }

}
