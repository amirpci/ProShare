package sidev17.siits.procks.Modul.Expert;

import sidev17.siits.procks.Modul.Worker.TambahPertanyaanWkr;
import sidev17.siits.procks.R;

public class TambahJawabanExprt extends TambahPertanyaanWkr {
    @Override
    protected void aturIdHalaman() {
        idHalaman= R.layout.activity_tambah_jawaban_exprt;
    }
}
