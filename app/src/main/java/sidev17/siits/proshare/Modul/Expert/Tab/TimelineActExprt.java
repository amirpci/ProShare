package sidev17.siits.proshare.Modul.Expert.Tab;

import sidev17.siits.proshare.Utils.ViewTool.MainAct_Header;
import sidev17.siits.proshare.Modul.Worker.Tab.ShareActWkr;

/**
 * Created by USER on 02/05/2018.
 */

public class TimelineActExprt extends ShareActWkr {
    @Override
    public void initHeader(){
//        final MainAct_Header mainAct= (MainAct_Header) actInduk;
//        actInduk.aturJudulHeader("Pustaka");
        actInduk.aturGambarOpsiHeader_Null(0);
    }

    @Override
    protected void keHalamanAwal() {}
}

