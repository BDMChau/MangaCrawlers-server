package serverapi.Helpers;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoundNumber {


    public float roundRatingManga(float stars) {
        int starDiv = (int) (stars / 1);

        float starMod = stars % 1;


        if (starMod > 0 && starMod < 0.5) {
            starMod = 0;
        } else if (starMod > 0.5 && starMod <= 1) {
            starMod = 1;
        } else if (starMod == 0.5) {

            starMod = 0.5F;
        }

        float roundoff = starDiv + starMod;
        return roundoff;
    }

}
