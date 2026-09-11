package co.wethinkcode.logisticsconnect;

public class Hub {
    private String hubId;
    private String province;
    private String sortingCenter;
    private String active;

    public Hub() {}

    public Hub(String hubId, String province, String sortingCenter, String active) {
        this.hubId = hubId;
        this.province = province;
        this.sortingCenter = sortingCenter;
        this.active = active;
    }



    public String getHubId() {
        return hubId;
    }

    public String getProvince() {
        return province;
    }

    public String getSortingCenter() {
        return sortingCenter;
    }

    public String getActive() {
        return active;
    }
}
