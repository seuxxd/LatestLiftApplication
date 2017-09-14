package internet;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public class LiftParams {
//    电梯参数
    public String temprature;
    public String humidity;
    public String light;
    public String distance;
    public String voice;
    public String angle;
    public String palstance;
    public String accelarate;

    public LiftParams(String voice,
                      String accelarate,
                      String angle,
                      String distance,
                      String humidity,
                      String light,
                      String palstance,
                      String temprature) {
        this.voice = voice;
        this.accelarate = accelarate;
        this.angle = angle;
        this.distance = distance;
        this.humidity = humidity;
        this.light = light;
        this.palstance = palstance;
        this.temprature = temprature;
    }

    public String getAccelarate() {
        return accelarate;
    }

    public String getAngle() {
        return angle;
    }

    public String getDistance() {
        return distance;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getLight() {
        return light;
    }

    public String getPalstance() {
        return palstance;
    }

    public String getTemprature() {
        return temprature;
    }

    public String getVoice() {
        return voice;
    }

    public void setAccelarate(String accelarate) {
        this.accelarate = accelarate;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public void setPalstance(String palstance) {
        this.palstance = palstance;
    }

    public void setTemprature(String temprature) {
        this.temprature = temprature;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
