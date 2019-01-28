package sidev17.siits.proshare;

import com.rmtheis.yandtran.language.Language;

public class Konstanta {
    public static final String PENGGUNA_PREFS = "UserSekarang";
    public static final String permasalahanKey = "Problem";
    public static final String penggunaKey = "User";
    public static final String solusiKey = "Solusi";
    public static final String recordPengguna = "RecordPengguna";
    public static final String pertanyaanSayaKey = "PertanyaanSaya";
    public static final String photoProfileKey = "PhotoProfile";
    public static final String ratingPengguna = "Rating";
    public static final String voteSolusi = "VoteSolusi";
    public static final String voteProblem = "VoteProblem";
    public static final String ROOT_URL = "http://atifmfirebase.000webhostapp.com/DBUtils/v1/";
    public static final String REGISTER_URL = ROOT_URL+"registerUser.php";
    public static final String TAMBAH_VIDEO_URL = ROOT_URL+"addVideo.php";
    public static final String TAMBAH_FOTO_URL = ROOT_URL+"addFoto.php";
    public static final String TAMBAH_PROBLEM_URL = ROOT_URL+"addProblem.php";
    public static final String TAMBAH_COMMENT_URL = ROOT_URL + "addKomen.php";
    public static final String TAMBAH_SOLUSI_URL = ROOT_URL + "addSolusi.php";
    public static final String TAMBAH_PROBLEM_FOTO_URL  = ROOT_URL + "addFoto.php";
    public static final String PERTANYAAN_SAYA_URL = ROOT_URL+"DaftarDitanyakan.php";
    public static final String DAFTAR_BIDANG = ROOT_URL + "DaftarBidang.php";
    public static final String DAFTAR_NEGARA = ROOT_URL + "DaftarNegara.php";
    public static final String DAFTAR_FOTO = ROOT_URL + "DaftarFoto.php";
    public static final String DAFTAR_VIDEO = ROOT_URL + "DAftarVideo.php";
    public static final String CARI_BIDANG = ROOT_URL + "CariBidang.php";
    public static final String BIDANGKU =  ROOT_URL + "Bidangku.php";
    public static final String TIMELINE_URL = ROOT_URL+"Timeline.php";
    public static final String SEARCH_URL = ROOT_URL+"SearchProblem.php";
    public static final String PROBLEM_VOTE = ROOT_URL+"VoteProblem.php";
    public static final String PROBLEM_VOTE_COUNT = ROOT_URL + "VoteCountProblem.php";
    public static final String SOLUTION_VOTE = ROOT_URL+"VoteSolution.php";
    public static final String SOLUTION_VOTE_COUNT = ROOT_URL + "VoteCountSolution.php";
    public static final String COMMENT_VOTE = ROOT_URL+"VoteComment.php";
    public static final String COMMENT_VOTE_COUNT = ROOT_URL + "VoteCountComment.php";
    public static final String SOLUTION = ROOT_URL + "Solusi.php";
    public static final String COMMENT = ROOT_URL + "Komentar.php";
    public static final String LOGIN_INTENT = "login_intent";
    public static final int LOGIN_LOGOUT = 311;
    public static final int LOGIN_REGISTER = 312;
    public static final Language BAHASA_INDONESIA = Language.INDONESIAN;
    public static final Language BAHASA_INGGRIS = Language.ENGLISH;
    public static final Language BAHASA_JEPANG = Language.JAPANESE;
    public static final int PROBLEM_STATUS_UNVERIFIED = 311;
    public static final int PROBLEM_STATUS_VERIFIED = 312;
    public static final int PROBLEM_STATUS_REJECTED = 313;
}
