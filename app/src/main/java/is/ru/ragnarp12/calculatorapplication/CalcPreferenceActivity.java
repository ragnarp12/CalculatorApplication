package is.ru.ragnarp12.calculatorapplication;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import is.ru.ragnar.calculatorapplication.R;

/**
 * Created by ragnar on 8/30/15.
 */
public class CalcPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
