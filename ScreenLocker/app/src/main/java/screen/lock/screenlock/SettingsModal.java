package screen.lock.screenlock;

public class SettingsModal {
    int icon;
    String primaryText,SecondaryText;
    boolean switchCheck,switchState;

    public SettingsModal(int icon, String primaryText, String secondaryText, boolean switchCheck, boolean switchState) {
        this.icon = icon;
        this.primaryText = primaryText;
        SecondaryText = secondaryText;
        this.switchCheck = switchCheck;
        this.switchState = switchState;
    }

    public int getIcon() {
        return icon;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public String getSecondaryText() {
        return SecondaryText;
    }

    public boolean isSwitchCheck() {
        return switchCheck;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public void setSecondaryText(String secondaryText) {
        SecondaryText = secondaryText;
    }

    public void setSwitchCheck(boolean switchCheck) {
        this.switchCheck = switchCheck;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }
}
