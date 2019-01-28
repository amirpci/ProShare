package sidev17.siits.proshare.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import sidev17.siits.proshare.Model.ChatListItem;
import sidev17.siits.proshare.Model.ChatPesanItem;
import sidev17.siits.proshare.R;

public class Terjemahan {
    Context c;
    final static String TERJEMAHAN_ERROR = "ALSJDF8OAHAAJLLJXC.NXVOA9E";

    public Terjemahan(Context c) {
        this.c = c;
    }

    public static int indexBahasa(Context c) {
        int index = 0;
        switch (Utilities.getUserBahasa(c)) {
            case "en":
                index = 0;
                break;
            case "id":
                index = 1;
                break;
            case "ja":
                index = 2;
                break;
        }
        return index;
    }

    public int terjemahkanChat(View v, ChatPesanItem pesan, String id_bahasa) {
        TerjemahanChat chatTerjemah = new TerjemahanChat(c, pesan);
        return chatTerjemah.initViewChat(v, id_bahasa);
    }

    public int terjemahkanDaftarChat(View v, ChatListItem pesan, String id_bahasa, String id_kita) {
        TerjemahanChat chatTerjemah = new TerjemahanChat(c, pesan, id_kita);
        return chatTerjemah.initViewDaftarChat(v, id_bahasa);
    }

    public void simpanTerjemahanChat(ChatPesanItem pesan, String terjemahan, String id_bahasa) {
        TerjemahanChat chatTerjemah = new TerjemahanChat(c, pesan);
        chatTerjemah.simpanTerjemahanChat(terjemahan, id_bahasa);
    }

    public void simpanTerjemahanDaftarChat(ChatListItem pesan, String terjemahan, String id_bahasa, String id_kita) {
        TerjemahanChat chatTerjemah = new TerjemahanChat(c, pesan, id_kita);
        chatTerjemah.simpanTerjemahanDaftarChat(terjemahan, id_bahasa);
    }

    public void perbaruiTerjemahanChat(String id, String terjemahan, String id_bahasa) {
        TerjemahanChat chatTerjemah = new TerjemahanChat(c);
        chatTerjemah.perbaruiTerjemahanChat(id, terjemahan,
                new KolomIsi<String>(TerjemahanChat.AtributTerjemahanChat.ISI_TERJEMAHAN, terjemahan),
                new KolomIsi<String>(TerjemahanChat.AtributTerjemahanChat.ID_BAHASA, id_bahasa));
    }

    public void perbaruiTerjemahanDaftarChat(String idKita, String idOrang, String terjemahan, String id_bahasa) {
        TerjemahanChat chatTerjemah = new TerjemahanChat(c);
        chatTerjemah.perbaruiTerjemahanDaftarChat(idKita, idOrang, terjemahan,
                new KolomIsi<String>(TerjemahanChat.AtributDaftarChat.ISI_TERJEMAHAN, terjemahan),
                new KolomIsi<String>(TerjemahanChat.AtributDaftarChat.ID_BAHASA, id_bahasa));
    }

    public static String terjemahkan(String kata, String dari, String ke) {
        String akar = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
        String kunciApi = "key=trnsl.1.1.20180519T160825Z.7299a6aefc25b5ed.686b9fb304f26f1dfa1977702787e8088567146b";
        String text = "text=" + kata.replace(" ", "%20");
        String bahasa = "lang=" + dari + "-" + ke;
        String format = "format=plain";
        String url = akar + kunciApi + "&" + text + "&" + bahasa + "&" + format;
        if (hasilTerjemahan(url) != TERJEMAHAN_ERROR)
            return hasilTerjemahan(url);
        else return kata;
    }

    private static String hasilTerjemahan(String url) {
        try {
            String respon = Utilities.dapatkanResponse(new URL(url));
            JSONObject objek = new JSONObject(respon);
            JSONArray objekArr = objek.getJSONArray("text");
            return objekArr.getString(0);
        } catch (Exception e) {
            e.printStackTrace();
            return TERJEMAHAN_ERROR;
        }
    }

    public class TerjemahanChat {
        private static final String TABEL_NAMA_CHAT = "terjemahan_chat";
        private static final String TABEL_NAMA_DAFTAR_CHAT = "terjemahan_daftar_chat";
        private Context konteks;
        private ArrayList<ChatPesanItem> daftarChat;
        private ChatPesanItem chat;
        private ChatListItem listChat;
        private String idKita;
        TerjemahanDbHelper terjemahanHelperChat;
        TerjemahanDbHelper terjemahanHelperDaftarChat;

        public TerjemahanChat(Context konteks, ArrayList<ChatPesanItem> daftarChat) {
            this.konteks = konteks;
            this.daftarChat = daftarChat;
            initTabel();
        }

        public TerjemahanChat(Context konteks, ChatPesanItem chat) {
            this.konteks = konteks;
            this.chat = chat;
            initTabel();
        }

        public TerjemahanChat(Context konteks, ChatListItem daftarChat, String idKita) {
            this.konteks = konteks;
            this.listChat = daftarChat;
            this.idKita = idKita;
            initTabelDaftarChat();
        }

        public TerjemahanChat(Context konteks) {
            this.konteks = konteks;
            initTabel();
        }

        public void initTabel() {
            final String QUERI_BUAT_TABEL = "CREATE TABLE " + TABEL_NAMA_CHAT + " ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AtributTerjemahanChat.ID + " TEXT," +
                    AtributTerjemahanChat.ISI_ASLI + " TEXT," +
                    AtributTerjemahanChat.ISI_TERJEMAHAN + " TEXT," +
                    AtributTerjemahanChat.ID_BAHASA + " TEXT," +
                    AtributTerjemahanChat.PENGIRIM + " TEXT," +
                    AtributTerjemahanChat.PENERIMA + " TEXT," +
                    AtributTerjemahanChat.WAKTU + " TEXT )";
            final String QUERI_DROP_TABEL = "DROP TABLE IF EXISTS " + TABEL_NAMA_CHAT;
            terjemahanHelperChat = new TerjemahanDbHelper(konteks, QUERI_BUAT_TABEL, QUERI_DROP_TABEL);
        }

        public void initTabelDaftarChat() {
            final String QUERI_BUAT_TABEL = "CREATE TABLE " + TABEL_NAMA_DAFTAR_CHAT + " ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AtributDaftarChat.ID_KITA + " TEXT," +
                    AtributDaftarChat.ID_ORANG + " TEXT," +
                    AtributDaftarChat.ID_BAHASA + " TEXT," +
                    AtributDaftarChat.ISI_ASLI + " TEXT," +
                    AtributDaftarChat.ISI_TERJEMAHAN + " TEXT )";
            final String QUERI_DROP_TABEL = "DROP TABLE IF EXISTS " + TABEL_NAMA_DAFTAR_CHAT;
            terjemahanHelperDaftarChat = new TerjemahanDbHelper(konteks, QUERI_BUAT_TABEL, QUERI_DROP_TABEL);
        }

        public SQLiteDatabase dapatkanDatabaseTulis() {
            return terjemahanHelperChat.getWritableDatabase();
        }

        protected SQLiteDatabase dapatkanDatabaseBaca() {
            return terjemahanHelperChat.getReadableDatabase();
        }

        public SQLiteDatabase dapatkanDatabaseTulisDaftar() {
            return terjemahanHelperDaftarChat.getWritableDatabase();
        }

        protected SQLiteDatabase dapatkanDatabaseBacaDaftar() {
            return terjemahanHelperDaftarChat.getReadableDatabase();
        }

        protected int initViewChat(View v, String id_bahasa) {
            v.findViewById(R.id.chat_latar_translated).setVisibility(View.VISIBLE);
            TextView txt_pesan = v.findViewById(R.id.chat_teks_translated);
            String hasilPencarian = cariTerjemahanChat(chat.getId(), id_bahasa);
            if (hasilPencarian == null)
                return TerjemahanDbHelper.TERJEMAHAN_TIDAK_DITEMUKAN;
            else if (hasilPencarian.equals(TerjemahanDbHelper.TERJEMAHAN_USANG))
                return TerjemahanDbHelper.TERJEMAHAN_PERLU_DIPERBARUI;
            else {
                if (!hasilPencarian.equalsIgnoreCase(chat.getPesan()))
                    txt_pesan.setText(hasilPencarian);
                else
                    v.findViewById(R.id.chat_latar_translated).setVisibility(View.GONE);
                return TerjemahanDbHelper.TERJEMAHAN_DITEMUKAN;
            }
        }

        protected int initViewDaftarChat(View v, String id_bahasa) {
            TextView chatPrev = v.findViewById(R.id.feedback_orang_chat);
            String hasilPencarian = cariTerjemahanDaftarChat(idKita, listChat.getOrang().getEmail(), id_bahasa);
            if (hasilPencarian == null)
                return TerjemahanDbHelper.TERJEMAHAN_TIDAK_DITEMUKAN;
            else if (hasilPencarian.equals(TerjemahanDbHelper.TERJEMAHAN_USANG))
                return TerjemahanDbHelper.TERJEMAHAN_PERLU_DIPERBARUI;
            else {
                if (!hasilPencarian.equalsIgnoreCase(listChat.getPrewMessage()))
                    chatPrev.setText(hasilPencarian);
                return TerjemahanDbHelper.TERJEMAHAN_DITEMUKAN;
            }
        }

        protected String cariTerjemahanChat(String id, String id_bahasa) {
            String kondisiPilihan = AtributTerjemahanChat.ID + " = ? AND " + AtributTerjemahanChat.ID_BAHASA + " = ?";
            String kondisiPilihanKedua = AtributTerjemahanChat.ID + " = ?";
            String[] kondisiArgumen = {id, id_bahasa};
            String[] kondisiArgumenKedua = {id};
            Cursor terjemahan = initCursor(dapatkanDatabaseBaca(), TABEL_NAMA_CHAT, kondisiPilihan, kondisiArgumen, TerjemahanDbHelper.URUTAN_TIDAK, null, AtributTerjemahanChat.ISI_TERJEMAHAN);
            Cursor terjemahanKedua = initCursor(dapatkanDatabaseBaca(), TABEL_NAMA_CHAT, kondisiPilihanKedua, kondisiArgumenKedua, TerjemahanDbHelper.URUTAN_TIDAK, null, AtributTerjemahanChat.ISI_TERJEMAHAN);
            if (terjemahan != null) {
                try {
                    while (terjemahan.moveToNext()) {
                        return terjemahan.getString(0);
                    }
                } finally {
                    terjemahan.close();
                }
            } else {
                if (terjemahanKedua == null) {
                    terjemahanKedua.close();
                    return null;
                }
                while (terjemahanKedua.moveToNext()) {
                    terjemahanKedua.close();
                    return TerjemahanDbHelper.TERJEMAHAN_USANG;
                }
            }
            terjemahan.close();
            return null;
        }

        protected String cariTerjemahanDaftarChat(String id_kita, String id_orang, String id_bahasa) {
            String kondisiPilihan = AtributDaftarChat.ID_KITA + " = ? AND " + AtributDaftarChat.ID_ORANG + " = ? AND " + AtributDaftarChat.ID_BAHASA + " = ?";
            String kondisiPilihanKedua = AtributDaftarChat.ID_KITA + " = ? AND " + AtributDaftarChat.ID_ORANG + " = ?";
            String[] kondisiArgumen = {id_kita, id_orang, id_bahasa};
            String[] kondisiArgumenKedua = {id_kita, id_orang};
            Cursor terjemahan = initCursor(dapatkanDatabaseBacaDaftar(), TABEL_NAMA_DAFTAR_CHAT, kondisiPilihan, kondisiArgumen, TerjemahanDbHelper.URUTAN_TIDAK, null, AtributDaftarChat.ISI_TERJEMAHAN);
            Cursor terjemahanKedua = initCursor(dapatkanDatabaseBacaDaftar(), TABEL_NAMA_DAFTAR_CHAT, kondisiPilihanKedua, kondisiArgumenKedua, TerjemahanDbHelper.URUTAN_TIDAK, null, AtributDaftarChat.ISI_TERJEMAHAN);
            if (terjemahan != null) {
                try {
                    while (terjemahan.moveToNext()) {
                        return terjemahan.getString(0);
                    }
                } finally {
                    terjemahan.close();
                }
            } else {
                if (terjemahanKedua == null) {
                    terjemahanKedua.close();
                    return null;
                }
                while (terjemahanKedua.moveToNext()) {
                    terjemahanKedua.close();
                    return TerjemahanDbHelper.TERJEMAHAN_USANG;
                }
            }
            return null;
        }

        protected void simpanTerjemahanChat(String terjemahan, String idBahasa) {
            KolomIsi<String>[] kolomIsi = new KolomIsi[]{
                    new KolomIsi<>(AtributTerjemahanChat.ID, chat.getId()),
                    new KolomIsi<>(AtributTerjemahanChat.ISI_ASLI, chat.getPesan()),
                    new KolomIsi<>(AtributTerjemahanChat.ISI_TERJEMAHAN, terjemahan),
                    new KolomIsi<>(AtributTerjemahanChat.ID_BAHASA, idBahasa),
                    new KolomIsi<>(AtributTerjemahanChat.PENGIRIM, chat.getPengirim()),
                    new KolomIsi<>(AtributTerjemahanChat.PENERIMA, chat.getPenerima()),
                    new KolomIsi<>(AtributTerjemahanChat.WAKTU, chat.getWaktu())
            };
            masukanKeTabel(dapatkanDatabaseTulis(), TABEL_NAMA_CHAT, kolomIsi);
        }

        protected void simpanTerjemahanDaftarChat(String terjemahan, String idBahasa) {
            KolomIsi<String>[] kolomIsi = new KolomIsi[]{
                    new KolomIsi<>(AtributDaftarChat.ID_KITA, idKita),
                    new KolomIsi<>(AtributDaftarChat.ID_ORANG, listChat.getOrang().getEmail()),
                    new KolomIsi<>(AtributDaftarChat.ID_BAHASA, idBahasa),
                    new KolomIsi<>(AtributDaftarChat.ISI_ASLI, listChat.getPrewMessage()),
                    new KolomIsi<>(AtributDaftarChat.ISI_TERJEMAHAN, terjemahan)
            };
            masukanKeTabel(dapatkanDatabaseTulisDaftar(), TABEL_NAMA_DAFTAR_CHAT, kolomIsi);
        }

        public void perbaruiTerjemahanChat(String id, String terjemahan, KolomIsi<String>... kolomIsi) {
            String kondisiPilihan = AtributTerjemahanChat.ID + " = ?";
            String kondisiArgumen[] = {id};
            ContentValues nilai = new ContentValues();
            for (int i = 0; i < kolomIsi.length; i++) {
                nilai.put(kolomIsi[i].kolom, kolomIsi[i].isi);
            }
            dapatkanDatabaseTulis().update(
                    TABEL_NAMA_CHAT,
                    nilai,
                    kondisiPilihan,
                    kondisiArgumen
            );
        }

        public void perbaruiTerjemahanDaftarChat(String idKita, String idOrang, String terjemahan, KolomIsi<String>... kolomIsi) {
            String kondisiPilihan = AtributDaftarChat.ID_KITA + " = ? AND " + AtributDaftarChat.ID_ORANG + " = ?";
            String kondisiArgumen[] = {idKita, idOrang};
            ContentValues nilai = new ContentValues();
            for (int i = 0; i < kolomIsi.length; i++) {
                nilai.put(kolomIsi[i].kolom, kolomIsi[i].isi);
            }
            dapatkanDatabaseTulisDaftar().update(
                    TABEL_NAMA_CHAT,
                    nilai,
                    kondisiPilihan,
                    kondisiArgumen
            );
        }

        protected Cursor initCursor(SQLiteDatabase db, String namaTabel, String kondisiPilihan, String[] kondisiArgumen, int urut, String kolomUrut, String... daftarKolom) {
            String urutan = "";
            switch (urut) {
                case TerjemahanDbHelper.URUTAN_TIDAK:
                    break;
                case TerjemahanDbHelper.URUTAN_NAIK:
                    urutan = kolomUrut + " ASC";
                    break;
                case TerjemahanDbHelper.URUTAN_TURUN:
                    urutan = kolomUrut + " DESC";
            }
            Cursor output = db.query(
                    namaTabel,
                    daftarKolom,
                    kondisiPilihan,
                    kondisiArgumen,
                    null, null,
                    urutan.equals("") ? null : urutan
            );

            return output;
        }

        protected ArrayList<ChatPesanItem> bacaTabel(String id) {
            ArrayList<ChatPesanItem> pesanTerbaca = new ArrayList<>();
            String atribut[] = {AtributTerjemahanChat.ID,
                    AtributTerjemahanChat.ISI_ASLI,
                    AtributTerjemahanChat.PENGIRIM,
                    AtributTerjemahanChat.PENERIMA,
                    AtributTerjemahanChat.WAKTU
            };
            Cursor cs = initCursor(dapatkanDatabaseBaca(), TABEL_NAMA_CHAT, null, null, TerjemahanDbHelper.URUTAN_TIDAK, null, atribut);

            while (cs.moveToNext()) {
                ChatPesanItem pesan = new ChatPesanItem();
                pesan.setId(cs.getString(cs.getColumnIndex(AtributTerjemahanChat.ID)));
                pesan.setPesan(cs.getString(cs.getColumnIndex(AtributTerjemahanChat.ISI_ASLI)));
                pesan.setPengirim(cs.getString(cs.getColumnIndex(AtributTerjemahanChat.PENGIRIM)));
                pesan.setPenerima(cs.getString(cs.getColumnIndex(AtributTerjemahanChat.PENERIMA)));
                pesan.setWaktu(cs.getString(cs.getColumnIndex(AtributTerjemahanChat.WAKTU)));

                pesanTerbaca.add(pesan);
            }
            cs.close();
            return pesanTerbaca;
        }

        public void masukanKeTabel(SQLiteDatabase db, String namaTabel, KolomIsi<String>... kolomIsi) {
            ContentValues nilai = new ContentValues();
            for (int i = 0; i < kolomIsi.length; i++) {
                nilai.put(kolomIsi[i].kolom, kolomIsi[i].isi);
            }
            db.insert(namaTabel, null, nilai);
        }

        public class AtributTerjemahanChat {
            public static final String ID = "id_chat";
            public static final String ISI_ASLI = "isi_asli";
            public static final String ISI_TERJEMAHAN = "isi_terjemahan";
            public static final String ID_BAHASA = "id_bahasa";
            public static final String PENGIRIM = "pengirim";
            public static final String PENERIMA = "penerima";
            public static final String WAKTU = "waktu";
        }

        public class AtributDaftarChat {
            public static final String ID_KITA = "id_chat_kita";
            public static final String ID_ORANG = "id_chat_orang";
            public static final String ISI_ASLI = "isi_asli";
            public static final String ISI_TERJEMAHAN = "isi_terjemahan";
            public static final String ID_BAHASA = "id_bahasa";
        }

    }

    public class KolomIsi<T> {
        private String kolom;
        private T isi;

        public KolomIsi(String kolom, T isi) {
            this.kolom = kolom;
            this.isi = isi;
        }

        public String getKolom() {
            return kolom;
        }

        public T getIsi() {
            return isi;
        }
    }

    public class TerjemahanDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSI = 1;
        public static final int DATABASE_VERSI_2 = 2;
        public static final int URUTAN_NAIK = 1234;
        public static final int URUTAN_TURUN = 4321;
        public static final int URUTAN_TIDAK = 1144;
        public static final int TERJEMAHAN_DITEMUKAN = 9832;
        public static final int TERJEMAHAN_TIDAK_DITEMUKAN = 8932;
        public static final int TERJEMAHAN_PERLU_DIPERBARUI = 7932;
        public static final String TERJEMAHAN_USANG = "LWEJ99823FOLWFHJOHFE9PRFA";
        public static final String TERJEMAHAN_TIDAK_PERLU = "LAJSFD98EFWOASDFJFF292OAJF";
        public static final String DATABASE_NAMA = "Terjemahan.db";
        public String QUERI_BUAT_TABEL, QUERI_DROP_TABEL;

        public TerjemahanDbHelper(Context context, String QUERI_BUAT_TABEL, String QUERI_DROP_TABEL) {
            super(context, DATABASE_NAMA, null, DATABASE_VERSI);
            this.QUERI_BUAT_TABEL = QUERI_BUAT_TABEL;
            this.QUERI_DROP_TABEL = QUERI_DROP_TABEL;
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(QUERI_BUAT_TABEL);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(QUERI_DROP_TABEL);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
