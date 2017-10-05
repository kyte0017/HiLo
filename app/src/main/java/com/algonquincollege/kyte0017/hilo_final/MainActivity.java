package com.algonquincollege.kyte0017.hilo_final;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.regex.Pattern;


public class MainActivity extends Activity {
    private static final String ABOUT_DIALOG_TAG = "About Dialog";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            DialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(getFragmentManager(), ABOUT_DIALOG_TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    final int MAX = 1000;
    final int MIN = 1;
    int guess_count = 0;
    int guess_max = 10;
    private int secretNum = 0;


    public int RandomNumber(){
        Random rn = new Random();
        int randomInt = rn.nextInt(MAX - MIN + 1) + MIN;
        return randomInt;
    }

    public void ToastMe_count(String Message){
        //ONLY COUNTING VALID GUESSES
        guess_count++;
        Toast.makeText( getApplicationContext(), Message + " Count: " + guess_count, Toast.LENGTH_SHORT ).show( );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submission = (Button) findViewById(R.id.submitBtn);
        //Initalizing the first Random Number
        secretNum = RandomNumber();


        Button restBtn = (Button) findViewById(R.id.reset_button);

        //RESET BUTTON, SHORT LISTENER
        restBtn.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                guess_count = 0;
                secretNum = RandomNumber();
                Toast.makeText( getApplicationContext(), "RESET", Toast.LENGTH_SHORT ).show( );
                return;
            }
        }));
        //RESET BUTTON, LONG LISTENER
        restBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                guess_count = 0;
                Toast.makeText( getApplicationContext(), "RESET, Number was " + secretNum, Toast.LENGTH_LONG ).show( );
                secretNum = RandomNumber();
                return true;
            }
        });


        //SUBMISSION BUTTON
        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText user_node = (EditText)findViewById(R.id.user_guess);

                String user_guess = user_node.getText().toString();

                //Isnt an int, return Error
                if(!(Pattern.matches("([0-9]{1}([0-9]+)?)", user_guess))) {
                    user_node.setError("Please enter a valid number");
                    user_node.requestFocus();
                    return;

                }else{
                    //It is an integer
                    int user_interger = Integer.parseInt(user_guess);
                    //Check to see if it's within the MAX and MIN
                    if(!(user_interger >= MIN && user_interger <= MAX)){
                        user_node.setError("Please Enter a number between " + MAX + " and " + MIN);
                        user_node.requestFocus();
                        return;
                    }else if (guess_count > guess_max){
                        guess_count = 0;
                        ToastMe_count("You've gone over the limit of " + guess_max + ". The number was " + secretNum);
                        //secretNum = RandomNumber();
                    }else if(user_interger > secretNum){
                        //TOAST TOO HIGH
                        ToastMe_count("TOO HIGH");
                    }else if(user_interger < secretNum){
                        //TOAST FUNCTION TOO LOW
                        ToastMe_count("TOO LOW");
                    }else{
                        //YOU WIN!
                        //RESET THE RANDOM NUMBER AND COUNT
                        user_node.setText("");
                        ToastMe_count("YOU GOT IT! It was " + secretNum + "! You took " + guess_count + " guesses");
                        secretNum = RandomNumber();
                        guess_count = 0;
                        return;

                    }


                }


            }

        });

    }
}
