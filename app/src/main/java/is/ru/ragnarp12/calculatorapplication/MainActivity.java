package is.ru.ragnarp12.calculatorapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.Stack;
import java.util.StringTokenizer;

import is.ru.ragnar.calculatorapplication.R;

public class MainActivity extends AppCompatActivity {

    private Vibrator m_vibrate;
    private TextView m_display;
    private Boolean m_use_vibrator = false;
    SharedPreferences m_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_display = (TextView) findViewById(R.id.display);
        m_vibrate = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        m_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        m_use_vibrator = m_sp.getBoolean("vibrate", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, CalcPreferenceActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonPressed(View view) {
        Button buttonView = (Button) view;
        String text = m_display.getText().toString();

        switch (view.getId()) {
            // All buttons should do the same,
            // that is they should put their text in m_display
            case R.id.button0:
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
                m_display.append(buttonView.getText());
                break;

            case R.id.buttonN:
            case R.id.buttonP:
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                if ( !(text.endsWith("+") || text.endsWith("-") || text.isEmpty()) ) {
                    m_display.append(buttonView.getText());
                }
                break;

            case R.id.buttonB:
                if (text.length() != 0) {
                    String newText = text.substring(0, text.length() - 1);
                    m_display.setText(newText);
                }
                break;

            case R.id.buttonC:
                // Clear the textView
                m_display.setText("");
                break;

            case R.id.buttonE:
                // User pressed Equals button
                // Result is compiled through RPN
                String result = evaluateExpression(m_display.getText().toString());
                m_display.setText(result);
                break;

        }

        if(m_use_vibrator) {
            m_vibrate.vibrate(25);
        }

    }

    private String evaluateExpression(String expr) {
        BigInteger result = BigInteger.ZERO;
        //String result = "";
        StringTokenizer st = new StringTokenizer( expr, "[+\\-]", true );
        Stack<BigInteger> numbers = new Stack<BigInteger>();
        Stack<String> signs = new Stack<String>();

        while ( st.hasMoreElements() ) {
            String token = st.nextToken();
            if (token.matches("\\d+")) {
                numbers.push(new BigInteger(token));
            }
            else {
                signs.push(token);
            }

        }
        Stack<BigInteger> revNumbers = new Stack<BigInteger>();
        Stack<String> revSigns = new Stack<String>();

        while(!numbers.empty()) {
            //Log.v("MYAPP", numbers.peek().toString());
            revNumbers.push(numbers.pop());
        }

        while(!signs.empty()){
            //Log.v("MYAPP", signs.peek().toString());
            revSigns.push(signs.pop());
        }

        while(revNumbers.size() >= 2) {
            BigInteger firstNumber = revNumbers.pop();
            BigInteger secondNumber = revNumbers.pop();

            String sign = revSigns.pop();

            if (sign.equals("+") ) {
                //Log.v("MYAPP", "Plus: "+firstNumber.add(secondNumber));
                revNumbers.push(firstNumber.add(secondNumber));
            }
            else {
                //Log.v("MYAPP", "Minus: "+firstNumber.subtract(secondNumber));
                revNumbers.push(firstNumber.subtract(secondNumber));
            }
        }

        return revNumbers.empty() ? "0" : revNumbers.pop().toString();
    }

}
