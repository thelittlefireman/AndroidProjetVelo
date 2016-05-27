package com.ppp.esir.projetvelo.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.heinrichreimersoftware.singleinputform.SingleInputFormActivity;
import com.heinrichreimersoftware.singleinputform.steps.DateStep;
import com.heinrichreimersoftware.singleinputform.steps.Step;
import com.heinrichreimersoftware.singleinputform.steps.TextStep;
import com.ppp.esir.projetvelo.R;
import com.ppp.esir.projetvelo.models.User;
import com.ppp.esir.projetvelo.requetes.Requete;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InscriptionActivity extends SingleInputFormActivity {
    private final static String LOGIN = "login";
    private static final String DATA_KEY_PASSWORD = "password";
    private static final String DATA_KEY_BIRTHDAY = "birthday";
    private static final String NOM = "nom";
    private static final String PRENOM = "prénom";
    private static final String DATA_KEY_EMAIL = "email";

    @Override
    protected List<Step> onCreateSteps() {
        List<Step> steps = new ArrayList<Step>();
        steps.add(new TextStep.Builder(this, LOGIN)
                .titleResId(R.string.login)
                .errorResId(R.string.login_error)
                .detailsResId(R.string.login_exemple)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return input.length() > 0;
                    }
                })
                .build());

        steps.add(new TextStep.Builder(this, DATA_KEY_EMAIL)
                .titleResId(R.string.email)
                .errorResId(R.string.email_error)
                .detailsResId(R.string.email_details)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
                    }
                })
                .build());

        steps.add(new TextStep.Builder(this, NOM)
                .titleResId(R.string.nom)
                .errorResId(R.string.nom_error)
                .detailsResId(R.string.nom_exemple)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .build());
        steps.add(new TextStep.Builder(this, PRENOM)
                .titleResId(R.string.prenom)
                .errorResId(R.string.error_prenom)
                .detailsResId(R.string.prenom_exemple)
                .inputType(InputType.TYPE_CLASS_TEXT)

                .build());
        steps.add(new TextStep.Builder(this, DATA_KEY_PASSWORD)
                .titleResId(R.string.password)
                .errorResId(R.string.password_error)
                .detailsResId(R.string.password_details)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .validator(new TextStep.Validator() {
                    @Override
                    public boolean validate(String input) {
                        return input.length() >= 5;
                    }
                })
                .build());

        steps.add(new DateStep.Builder(this, DATA_KEY_BIRTHDAY)
                .titleResId(R.string.birthday)
                .errorResId(R.string.birthday_error)
                .detailsResId(R.string.birthday_details)
                .validator(new DateStep.Validator() {
                    @Override
                    public boolean validate(int year, int month, int day) {
                        Calendar today = new GregorianCalendar();
                        Calendar birthday = new GregorianCalendar(year, month, day);
                        today.add(Calendar.YEAR, -14);
                        return today.after(birthday);
                    }
                })
                .build());


        //Add more steps here...
        return steps;
    }

    @Override
    protected void onFormFinished(Bundle data) {
        Toast.makeText(this, "Form finished: " +
                        TextStep.text(data, DATA_KEY_EMAIL) + ", " +
                        TextStep.text(data, LOGIN) + ", " +
                        TextStep.text(data, DATA_KEY_PASSWORD) + ", " +
                        DateStep.day(data, DATA_KEY_BIRTHDAY) + "." + DateStep.month(data, DATA_KEY_BIRTHDAY) + "." + DateStep.year(data, DATA_KEY_BIRTHDAY) + ", " +
                        TextStep.text(data, NOM) + ", " +
                        TextStep.text(data, PRENOM),
                Toast.LENGTH_LONG).show();
        Log.d("MainActivity", "data: " + data.toString());

        Calendar dob = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateOfBirth = null;
        try {
            dateOfBirth = sdf.parse(DateStep.day(data, DATA_KEY_BIRTHDAY) + "/" + DateStep.month(data, DATA_KEY_BIRTHDAY) + "/" + DateStep.year(data, DATA_KEY_BIRTHDAY));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dob.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
            age--;

        //Wait 4 seconds and finish
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 4000);
        User user = new User(TextStep.text(data, LOGIN), TextStep.text(data, DATA_KEY_PASSWORD), TextStep.text(data, NOM), TextStep.text(data, PRENOM), String.valueOf(age), TextStep.text(data, DATA_KEY_EMAIL));
        InscriptionTask inscriptionTask = new InscriptionTask(user);
        inscriptionTask.execute();
        try {
            inscriptionTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public class InscriptionTask extends AsyncTask<Void, Void, Integer> {

        private final User user;

        InscriptionTask(User user) {
            this.user = user;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (Requete.inscription(user) == 1) {
                //TODO SAVE login/Password
                return 1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(final Integer rst) {

        }

        @Override
        protected void onCancelled() {

        }
    }
}
