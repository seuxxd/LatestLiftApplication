package jsonmodel;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public class LiftParams {
//    电梯参数
    public double temperature;
    public double humidity;
    public double light;
    public double distance;
    public double voice;
    public String[] voltage;
    public String[] Amper;
    public String[] dis;
    public String[] angle;
    public String[] pal;
    public String[] acc;

    public LiftParams() {

    }

    public void setAcc(String[] acc) {
        this.acc = acc;
    }

    public void setAmper(String[] amper) {
        this.Amper = amper;
    }

    public void setAngle(String[] angle) {
        this.angle = angle;
    }

    public void setDis(String[] dis) {
        this.dis = dis;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public void setPal(String[] pal) {
        this.pal = pal;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setVoice(double voice) {
        this.voice = voice;
    }

    public void setVoltage(String[] voltage) {
        this.voltage = voltage;
    }

    public String[] getAcc() {

        return acc;
    }

    public String[] getAmper() {
        return Amper;
    }

    public String[] getAngle() {
        return angle;
    }

    public String[] getDis() {
        return dis;
    }

    public double getDistance() {
        return distance;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getLight() {
        return light;
    }

    public String[] getPal() {
        return pal;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getVoice() {
        return voice;
    }

    public String[] getVoltage() {
        return voltage;
    }

    @Override
    public String toString() {
        return  "temperature: " + temperature      + "\n" +
                "humidity: "    + humidity         + "\n" +
                "light: "       + light            + "\n" +
                "distance: "    + distance         + "\n" +
                "voice: "       + voice            + "\n" +
                "angle[0]: "    + angle[0]         + "\n" +
                "angle[1]: "    + angle[1]         + "\n" +
                "angle[2]: "    + angle[2]         + "\n" +
                "Amper[0]: "    + Amper[0]         + "\n" +
                "Amper[1]: "    + Amper[0]         + "\n" +
                "Amper[2]: "    + Amper[0]         + "\n" +
                "voltage[0]: "  + voltage[0]       + "\n" +
                "voltage[1]: "  + voltage[1]       + "\n" +
                "voltage[2]: "  + voltage[2]       + "\n" +
                "dis[0]: "      + dis[0]           + "\n" +
                "dis[1]: "      + dis[1]           + "\n" +
                "dis[2]: "      + dis[2]           + "\n" +
                "pal[0]: "      + pal[0]           + "\n" +
                "pal[1]: "      + pal[1]           + "\n" +
                "pal[2]: "      + pal[2]           + "\n" +
                "acc[0]: "      + acc[0]           + "\n" +
                "acc[1]: "      + acc[1]           + "\n" +
                "acc[2]: "      + acc[2]           + "\n" ;
    }
}
