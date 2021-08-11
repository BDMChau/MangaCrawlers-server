package serverapi.helpers;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoundNumber {


    public float roundRatingManga(float stars) {
        int starDiv = (int) (stars / 1);

        float starMod = stars % 1;


        if (starMod >= 0.0F && starMod < 0.4F) {
            starMod = 0.0F;
        } else if (starMod >= 0.4F && starMod < 0.8F) {
            starMod = 0.5F;
        } else if (starMod >= 0.8F && starMod <= 1.0F) {

            starMod = 1.0F;
        }

        float roundoff = starDiv + starMod;
        return roundoff;
    }

}
