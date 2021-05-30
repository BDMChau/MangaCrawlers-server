package serverapi.Helpers;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoundNumber {


    public float roundRatingManga(float stars) {
        int starDiv = (int) (stars / 1);

        float starMod = stars % 1;


        if (starMod >= 0 && starMod < 0.4) {
            starMod = 0;
        } else if (starMod >= 0.4 && starMod < 0.8) {
            starMod = 0.5F;
        } else if (starMod >= 0.8 && starMod <= 1) {

            starMod = 1;
        }

        float roundoff = starDiv + starMod;
        return roundoff;
    }

}
