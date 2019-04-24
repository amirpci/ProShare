package sidev17.siits.proshare.Modul.Expert;

import sidev17.siits.proshare.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.proshare.R;

public class TambahReviewExprt extends TambahJawabanExprt {
    @Override
    protected void aturJenisPost() {
        jenisPost= JENIS_POST_REVIEW;
    }
}
