package com.admin.assessment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.admin.assessment.QuizContract.QuestionsTable;

import java.util.ArrayList;
import java.util.List;


public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Questions.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("How many days to be lapsed for employees in M1 & above category?",
                "14", "16", "18", 1);
        addQuestion(q1);
        Question q2 = new Question("How many days of PL can be accumulated for employees’ upto O4 grade?",
                "45", "30", "50", 2);
        addQuestion(q2);
        Question q3 = new Question("For how many months the Acturial value is considered?",
                "Jan-Mar", "Apr-Jun", "Jan-Apr", 1);
        addQuestion(q3);
        Question q4 = new Question("How many PL days can be accumulated for M1 & above grade employees?",
                "90", "upto 100", "100", 1);
        addQuestion(q4);
        Question q5 = new Question("What are the eligible leaves for upto O4 category?",
                "CL,SL,PL", "SL,PL", "PL only", 1);
        addQuestion(q5);
        Question q6 = new Question("What are the eligible leaves for M1 & above category?",
                "CL,SL,PL", "SL,PL", "PL only", 3);
        addQuestion(q6);
        Question q7 = new Question("Leave encashment formula",
                "Basic* No. of days", "Other allowance* No. of days", "Basic/30 * No. of days", 3);
        addQuestion(q7);
        Question q8 = new Question("How many days leave are eligible for marriage benefit?",
                "2", "5", "3", 3);
        addQuestion(q8);
        Question q9 = new Question("When will an employee become eligible for PL?",
                "After joining", "After confirmation", "After 1 year of service", 3);
        addQuestion(q9);
        Question q10 = new Question("An employee joins the company on 18th of August.Can we give leave credit for the month?",
                "Yes", "No", "Maybe", 2);
        addQuestion(q10);
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
