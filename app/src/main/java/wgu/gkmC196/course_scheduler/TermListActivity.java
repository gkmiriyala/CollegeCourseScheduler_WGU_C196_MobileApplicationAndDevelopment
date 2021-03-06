package wgu.gkmC196.course_scheduler;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.GregorianCalendar;

public class TermListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int TERM_EDITOR_ACTIVITY_CODE = 11111;
    public static final int TERM_VIEWER_ACTIVITY_CODE = 22222;

    private CursorAdapter cursorAdapter;
    private DataProvider database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(wgu.gkmC196.course_scheduler.R.layout.activity_term_list);
        Toolbar toolbar = (Toolbar) findViewById(wgu.gkmC196.course_scheduler.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] from = {DBOpenHelper.TERM_NAME, DBOpenHelper.TERM_START, DBOpenHelper.TERM_END};
        int[] to = {wgu.gkmC196.course_scheduler.R.id.tvTerm, wgu.gkmC196.course_scheduler.R.id.tvTermStartDate, wgu.gkmC196.course_scheduler.R.id.tvTermEndDate};

        cursorAdapter = new SimpleCursorAdapter(this, wgu.gkmC196.course_scheduler.R.layout.term_list_item, null, from, to, 0);
        database = new DataProvider();

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermListActivity.this, TermViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.TERMS_URI + "/" + id);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, TERM_VIEWER_ACTIVITY_CODE);
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            restartLoader();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(wgu.gkmC196.course_scheduler.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case wgu.gkmC196.course_scheduler.R.id.action_create_sample:
                return createSampleData();
            case wgu.gkmC196.course_scheduler.R.id.action_delete_all_terms:
                return deleteAllTerms();
            case wgu.gkmC196.course_scheduler.R.id.action_create_test_alarm:
                return createTestAlarm();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean createSampleData() {
        Uri term1Uri = DataManager.insertTerm(this, "Fall 2017", "2017-11-01", "2018-04-31", 0);
        Uri term2Uri = DataManager.insertTerm(this, "Spring 2018", "2018-05-01", "2018-10-31", 0);
        Uri term3Uri = DataManager.insertTerm(this, "Fall 2018", "2018-11-01", "2019-04-31", 1);


        DataManager.insertCourse(this, Long.parseLong(term1Uri.getLastPathSegment()),
                "C393 - IT Foundations", "2017-11-01", "2017-4-31",
                "Joshua Cousins", "(877) 435-8475", "Jocous@wgu.edu",
                CourseStatus.COMPLETED);

        DataManager.insertCourse(this, Long.parseLong(term2Uri.getLastPathSegment()),
                "C394 - IT Applications", "2017-05-01", "2017-05-31",
                "Joshua Cousins", "(877) 435-8475", "Jocous@wgu.edu",
                CourseStatus.COMPLETED);

        Uri course1Uri = DataManager.insertCourse(this, Long.parseLong(term3Uri.getLastPathSegment()),
                "C195 - Software II - Advanced Java Concepts", "2018-11-01", "2018-11-13",
                "Course Mentor Group", " ", "cmprogramming@wgu.edu",
                CourseStatus.COMPLETED);

        DataManager.insertCourse(this, Long.parseLong(term3Uri.getLastPathSegment()),
                "C856 - User Experience Design", "2018-11-01", "2018-11-30",
                "Joshua Cousins", "(877) 435-8475", "Jocous@wgu.edu",
                CourseStatus.IN_PROGRESS);

        DataManager.insertCourse(this, Long.parseLong(term3Uri.getLastPathSegment()),
                "C196 - Mobile Application Development", "2018-11-01", "2018-11-30",
                "Course Mentor Group", "", "cmprogramming@wgu.edu",
                CourseStatus.IN_PROGRESS);

        DataManager.insertCourse(this, Long.parseLong(term3Uri.getLastPathSegment()),
                "C868 - Software Development Capstone", "2018-11-01", "2018-11-30",
                "Course Mentor Group", "", "cmprogramming@wgu.edu",
                CourseStatus.IN_PROGRESS);

        DataManager.insertCourseNote(this, Long.parseLong(course1Uri.getLastPathSegment()),
                "This is a short test note");

        DataManager.insertCourseNote(this, Long.parseLong(course1Uri.getLastPathSegment()),
                getString(wgu.gkmC196.course_scheduler.R.string.long_test_note));

        Uri ass1Uri = DataManager.insertAssessment(this, Long.parseLong(course1Uri.getLastPathSegment()), "JKI8", "Mobile Application Development",
                "As a competent mobile application developer, your understanding of mobile application structure " +
                        "and design will help you to develop applications to meet customer requirements. The following " +
                        "project to develop a student scheduler/student progress tracking application, will help you to " +
                        "apply these skills in a familiar, real-world scenario. This task will allow you to demonstrate " +
                        "your ability to apply the skills learned in the course.\n \nYou will develop a multiple-screen " +
                        "mobile application for WGU students to track their terms, courses associated with each term, and " +
                        "assessment(s) associated with each course. The application will allow students to enter, edit, " +
                        "and delete term, course, and assessment data. It should provide summary and detailed views of " +
                        "courses for each term and provide alerts for upcoming performance and objective assessments. " +
                        "This application will use a SQLite database.", "2018-10-01 2:30 PM");

        Uri ass2Uri = DataManager.insertAssessment(this, Long.parseLong(course1Uri.getLastPathSegment()), "ALC3", "Second Assessment, although this one has a name that won't fit on the grid",
                "Assessment Description",  "2018-11-17 12:30 PM");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass1Uri.getLastPathSegment()),
                "Assessment #1 Note #1");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass1Uri.getLastPathSegment()),
                "Assessment #1 Note #2");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass2Uri.getLastPathSegment()),
                "Assessment #2 Note #1");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass2Uri.getLastPathSegment()),
                "Assessment #2 Note #2");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass2Uri.getLastPathSegment()),
                "Assessment #2 Note #3");

        restartLoader();
        return true;
    }

    private boolean deleteAllTerms() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    getContentResolver().delete(DataProvider.TERMS_URI, null, null);
                    getContentResolver().delete(DataProvider.COURSES_URI, null, null);
                    getContentResolver().delete(DataProvider.COURSE_NOTES_URI, null, null);
                    getContentResolver().delete(DataProvider.ASSESSMENTS_URI, null, null);
                    getContentResolver().delete(DataProvider.ASSESSMENT_NOTES_URI, null, null);
                    restartLoader();
                    Toast.makeText(TermListActivity.this, getString(wgu.gkmC196.course_scheduler.R.string.all_terms_deleted), Toast.LENGTH_SHORT).show();
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(wgu.gkmC196.course_scheduler.R.string.confirm_delete_all_terms))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
        return true;
    }

    private boolean createTestAlarm() {
        // Sets alarm for 5 seconds in the future
        Long time = new GregorianCalendar().getTimeInMillis() + 5000;

        Intent intent = new Intent(this, AlarmHandler.class);
        intent.putExtra("title", "Test Alarm");
        intent.putExtra("text", "This is a test alarm.");

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT));
        Toast.makeText(this, getString(wgu.gkmC196.course_scheduler.R.string.test_alarm), Toast.LENGTH_SHORT).show();
        return true;
    }

    public void openNewTermEditor(View view) {
        Intent intent = new Intent(this, TermEditorActivity.class);
        startActivityForResult(intent, TERM_EDITOR_ACTIVITY_CODE);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.TERMS_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
