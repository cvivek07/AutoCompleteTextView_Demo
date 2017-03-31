package in.android2.com.autocompletetextview_demo;

public class MutualFund_CustomClass {
    private String fundId;
    private String fundName;

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    @Override
    //override toString() method for returning the fund names. This will show the fund names in the suggestion list.
    //If you want to show the fund ids replace fundName with fundId.
    public String toString() {
        return fundName;
    }
}
