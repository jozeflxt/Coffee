package com.lexot.cenicafe;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.lexot.cenicafe.ContentProvider.BatchContract;
import com.lexot.cenicafe.ContentProvider.FrameContract;
import com.lexot.cenicafe.Models.BLL;
import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Utils.Helpers;
import com.lexot.cenicafe.Utils.Utilities;

public class NewBatchActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText ageEditText;
    private EditText treeEditText;
    private EditText branchEditText;
    private EditText stemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_batch);
        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        treeEditText = findViewById(R.id.treeEditText);
        branchEditText = findViewById(R.id.branchEditText);
        stemEditText = findViewById(R.id.stemEditText);

    }

    public void newBatch(View view) {
        Helpers.showLoading(this, "Cargando", "Cargando Lotes");
        String error = "";
        Integer age = Utilities.parseWithDefault(ageEditText.getText().toString(),0);
        Integer branchesAmount = Utilities.parseWithDefault(branchEditText.getText().toString(),0);
        Integer stems = Utilities.parseWithDefault(stemEditText.getText().toString(),0);
        Integer trees = Utilities.parseWithDefault(treeEditText.getText().toString(),0);
        if (nameEditText.getText().toString().isEmpty()) { error = "El nombre del lote es requerido";
        }
        if (!(branchesAmount > 0)) {
            error = "El número de ramas es requerido";
        }
        if (!(trees > 0)) {
            error = "El número de arboles es requerido";
        }
        if (!(stems > 0)) {
            error = "Debe especificar el número de tallos";
        }
        if (stems > 2) {
            error = "El número de tallos no puede superar 2";
        }
        if (branchesAmount > 99) {
            error = "El número de ramas no puede superar 99";
        }
        if (trees > 99) {
            error = "El número de árboles no puede superar 99";
        }
        if (error.equals("")) {
            CoffeeBatch coffeeBatch = new CoffeeBatch();
            coffeeBatch.Age = age;
            coffeeBatch.BranchesAmmount = branchesAmount;
            coffeeBatch.Name = nameEditText.getText().toString();
            coffeeBatch.Stems = stems;
            coffeeBatch.Trees = trees;
            new BLL(this).createBatch(coffeeBatch);
            Helpers.hideLoading();
            new AwesomeSuccessDialog(this)
                    .setTitle("Listo")
                    .setMessage("El lote fue agregado con éxito")
                    .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                    .setCancelable(false)
                    .setPositiveButtonText(getString(R.string.dialog_ok_button))
                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setPositiveButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    })
                    .show();
        } else {
            Helpers.hideLoading();
            new AwesomeErrorDialog(this)
                    .setTitle("Oppss...")
                    .setMessage(error)
                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(true)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            // click
                        }
                    })
                    .show();
        }
    }
}
