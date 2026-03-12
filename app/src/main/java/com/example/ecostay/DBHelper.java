package com.example.ecostay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EcoStayDB";
    private static final int DATABASE_VERSION = 12; // Fresh version to reset all tables

    // Table Names
    private static final String TABLE_USER = "users";
    private static final String TABLE_ROOM = "rooms";
    private static final String TABLE_ACTIVITY = "activities";
    private static final String TABLE_BOOKING = "bookings";
    private static final String TABLE_ACTIVITY_BOOKING = "activity_bookings";
    private static final String TABLE_NOTIFICATION = "notifications";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRICE = "price";
    private static final String KEY_AVAILABLE = "available";
    private static final String KEY_CAPACITY = "capacity";
    private static final String KEY_DATE = "date";

    // User Table Columns
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PREFERENCES = "preferences";
    private static final String KEY_IS_ADMIN = "is_admin";

    // Room Table Columns
    private static final String KEY_ROOM_TYPE = "room_type";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_AMENITIES = "amenities";

    // Activity Table Columns
    private static final String KEY_ACTIVITY_TYPE = "activity_type";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_GUIDE_NAME = "guide_name";

    // Booking Table Columns
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROOM_ID = "room_id";
    private static final String KEY_CHECK_IN = "check_in";
    private static final String KEY_CHECK_OUT = "check_out";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_STATUS = "status";

    // Activity Booking Table Columns
    private static final String KEY_ACTIVITY_ID = "activity_id";
    private static final String KEY_BOOKING_DATE = "booking_date";
    private static final String KEY_PARTICIPANTS = "participants";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_PREFERENCES + " TEXT,"
                + KEY_IS_ADMIN + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_ROOMS_TABLE = "CREATE TABLE " + TABLE_ROOM + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_ROOM_TYPE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_AVAILABLE + " INTEGER,"
                + KEY_CAPACITY + " INTEGER,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_AMENITIES + " TEXT" + ")";
        db.execSQL(CREATE_ROOMS_TABLE);

        String CREATE_ACTIVITIES_TABLE = "CREATE TABLE " + TABLE_ACTIVITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_ACTIVITY_TYPE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_AVAILABLE + " INTEGER,"
                + KEY_CAPACITY + " INTEGER,"
                + KEY_DURATION + " TEXT,"
                + KEY_GUIDE_NAME + " TEXT" + ")";
        db.execSQL(CREATE_ACTIVITIES_TABLE);

        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKING + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_ROOM_ID + " INTEGER,"
                + KEY_CHECK_IN + " TEXT,"
                + KEY_CHECK_OUT + " TEXT,"
                + KEY_TOTAL_PRICE + " REAL,"
                + KEY_STATUS + " TEXT,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_ROOM_ID + ") REFERENCES " + TABLE_ROOM + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        String CREATE_ACTIVITY_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_ACTIVITY_BOOKING + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_ACTIVITY_ID + " INTEGER,"
                + KEY_BOOKING_DATE + " TEXT,"
                + KEY_PARTICIPANTS + " INTEGER,"
                + KEY_STATUS + " TEXT,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_ACTIVITY_ID + ") REFERENCES " + TABLE_ACTIVITY + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_ACTIVITY_BOOKINGS_TABLE);

        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + "is_read INTEGER DEFAULT 0,"
                + "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_ID + ")" + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        
        values.put(KEY_NAME, "Mountain View Cabin");
        values.put(KEY_ROOM_TYPE, "Cabin");
        values.put(KEY_DESCRIPTION, "Cozy wooden cabin with stunning mountain views");
        values.put(KEY_PRICE, 199.99); 
        values.put(KEY_AVAILABLE, 1);
        values.put(KEY_CAPACITY, 4);
        values.put(KEY_IMAGE_URL, "cabin");
        values.put(KEY_AMENITIES, "WiFi,Kitchen,Fireplace");
        db.insert(TABLE_ROOM, null, values);

        values = new ContentValues();
        values.put(KEY_NAME, "Eco-Pod");
        values.put(KEY_ROOM_TYPE, "Eco-Pod");
        values.put(KEY_DESCRIPTION, "Sustainable living pod with solar power");
        values.put(KEY_PRICE, 149.99); 
        values.put(KEY_AVAILABLE, 1);
        values.put(KEY_CAPACITY, 2);
        values.put(KEY_IMAGE_URL, "ecopod");
        values.put(KEY_AMENITIES, "Solar Power,Composting Toilet,Skylight");
        db.insert(TABLE_ROOM, null, values);

        values = new ContentValues();
        values.put(KEY_NAME, "Treehouse Suite");
        values.put(KEY_ROOM_TYPE, "Treehouse");
        values.put(KEY_DESCRIPTION, "Luxurious treehouse surrounded by nature");
        values.put(KEY_PRICE, 299.99); 
        values.put(KEY_AVAILABLE, 1);
        values.put(KEY_CAPACITY, 3);
        values.put(KEY_IMAGE_URL, "treehouse");
        values.put(KEY_AMENITIES, "Private Deck,Outdoor Shower,Hammock");
        db.insert(TABLE_ROOM, null, values);

        // Sample Activities
        values = new ContentValues();
        values.put(KEY_NAME, "Guided Mountain Hike");
        values.put(KEY_ACTIVITY_TYPE, "Hiking");
        values.put(KEY_DESCRIPTION, "Expert-guided hike through mountain trails");
        values.put(KEY_PRICE, 45.00); 
        values.put(KEY_AVAILABLE, 1);
        values.put(KEY_CAPACITY, 10);
        values.put(KEY_DURATION, "4 hours");
        values.put(KEY_GUIDE_NAME, "John Mountain");
        db.insert(TABLE_ACTIVITY, null, values);

        values = new ContentValues();
        values.put(KEY_NAME, "Sustainability Workshop");
        values.put(KEY_ACTIVITY_TYPE, "Workshop");
        values.put(KEY_DESCRIPTION, "Learn about sustainable living practices");
        values.put(KEY_PRICE, 25.00); 
        values.put(KEY_AVAILABLE, 1);
        values.put(KEY_CAPACITY, 20);
        values.put(KEY_DURATION, "2 hours");
        values.put(KEY_GUIDE_NAME, "Emma Green");
        db.insert(TABLE_ACTIVITY, null, values);

        values = new ContentValues();
        values.put(KEY_NAME, "Bird Watching Tour");
        values.put(KEY_ACTIVITY_TYPE, "Nature Tour");
        values.put(KEY_DESCRIPTION, "Spot local bird species with expert guide");
        values.put(KEY_PRICE, 35.00); 
        values.put(KEY_AVAILABLE, 1);
        values.put(KEY_CAPACITY, 8);
        values.put(KEY_DURATION, "3 hours");
        values.put(KEY_GUIDE_NAME, "Sarah Wings");
        db.insert(TABLE_ACTIVITY, null, values);

        // Admin User
        values.clear();
        values.put(KEY_NAME, "Admin");
        values.put(KEY_EMAIL, "admin@ecostay.com");
        values.put(KEY_PASSWORD, "admin123");
        values.put(KEY_PHONE, "0000000000");
        values.put(KEY_PREFERENCES, "");
        values.put(KEY_IS_ADMIN, 1);
        db.insert(TABLE_USER, null, values);

        // Regular Guest User
        values.clear();
        values.put(KEY_NAME, "Guest");
        values.put(KEY_EMAIL, "guest@example.com");
        values.put(KEY_PASSWORD, "guest123");
        values.put(KEY_PHONE, "1234567890");
        values.put(KEY_PREFERENCES, "");
        values.put(KEY_IS_ADMIN, 0);
        db.insert(TABLE_USER, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY_BOOKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);
    }

    public boolean registerUser(String name, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_PHONE, phone);
        values.put(KEY_PREFERENCES, "");
        values.put(KEY_IS_ADMIN, 0);
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID},
                KEY_EMAIL + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_ID},
                KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    public User getUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, KEY_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            user.setAdmin(cursor.getInt(6) == 1);
        }
        cursor.close();
        db.close();
        return user;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_PREFERENCES, user.getPreferences());
        int result = db.update(TABLE_USER, values, KEY_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return result > 0;
    }

    public boolean isAdmin(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_IS_ADMIN},
                KEY_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        boolean isAdmin = false;
        if (cursor.moveToFirst()) {
            isAdmin = cursor.getInt(0) == 1;
        }
        cursor.close();
        db.close();
        return isAdmin;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                user.setAdmin(cursor.getInt(6) == 1);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKING, KEY_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.delete(TABLE_ACTIVITY_BOOKING, KEY_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.delete(TABLE_NOTIFICATION, KEY_USER_ID + "=?", new String[]{String.valueOf(userId)});
        int result = db.delete(TABLE_USER, KEY_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    public List<Room> getAllRooms() {
        List<Room> roomList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ROOM + " WHERE " + KEY_AVAILABLE + "=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                room.setId(cursor.getInt(0));
                room.setName(cursor.getString(1));
                room.setType(cursor.getString(2));
                room.setDescription(cursor.getString(3));
                room.setPrice(cursor.getDouble(4));
                room.setAvailable(cursor.getInt(5) == 1);
                room.setCapacity(cursor.getInt(6));
                room.setImageUrl(cursor.getString(7));
                room.setAmenities(cursor.getString(8));
                roomList.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return roomList;
    }

    public List<Room> filterRooms(String type, double maxPrice) {
        List<Room> roomList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ROOM + " WHERE " + KEY_AVAILABLE + "=1";
        if (type != null && !type.isEmpty()) {
            query += " AND " + KEY_ROOM_TYPE + "='" + type + "'";
        }
        if (maxPrice > 0) {
            query += " AND " + KEY_PRICE + "<=" + maxPrice;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                room.setId(cursor.getInt(0));
                room.setName(cursor.getString(1));
                room.setType(cursor.getString(2));
                room.setDescription(cursor.getString(3));
                room.setPrice(cursor.getDouble(4));
                room.setAvailable(cursor.getInt(5) == 1);
                room.setCapacity(cursor.getInt(6));
                room.setImageUrl(cursor.getString(7));
                room.setAmenities(cursor.getString(8));
                roomList.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return roomList;
    }

    public Room getRoom(int roomId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROOM, null, KEY_ID + "=?",
                new String[]{String.valueOf(roomId)}, null, null, null);
        Room room = null;
        if (cursor.moveToFirst()) {
            room = new Room(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getInt(5) == 1,
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
        }
        cursor.close();
        db.close();
        return room;
    }

    public boolean addRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, room.getName());
        values.put(KEY_ROOM_TYPE, room.getType());
        values.put(KEY_DESCRIPTION, room.getDescription());
        values.put(KEY_PRICE, room.getPrice());
        values.put(KEY_AVAILABLE, room.isAvailable() ? 1 : 0);
        values.put(KEY_CAPACITY, room.getCapacity());
        values.put(KEY_IMAGE_URL, room.getImageUrl());
        values.put(KEY_AMENITIES, room.getAmenities());
        long result = db.insert(TABLE_ROOM, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateRoom(Room room) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, room.getName());
        values.put(KEY_ROOM_TYPE, room.getType());
        values.put(KEY_DESCRIPTION, room.getDescription());
        values.put(KEY_PRICE, room.getPrice());
        values.put(KEY_AVAILABLE, room.isAvailable() ? 1 : 0);
        values.put(KEY_CAPACITY, room.getCapacity());
        values.put(KEY_IMAGE_URL, room.getImageUrl());
        values.put(KEY_AMENITIES, room.getAmenities());
        int result = db.update(TABLE_ROOM, values, KEY_ID + "=?", new String[]{String.valueOf(room.getId())});
        db.close();
        return result > 0;
    }

    public boolean deleteRoom(int roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ROOM, KEY_ID + "=?", new String[]{String.valueOf(roomId)});
        db.close();
        return result > 0;
    }

    public List<Activity> getAllActivities() {
        List<Activity> activityList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + KEY_AVAILABLE + "=1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity.setId(cursor.getInt(0));
                activity.setName(cursor.getString(1));
                activity.setType(cursor.getString(2));
                activity.setDescription(cursor.getString(3));
                activity.setPrice(cursor.getDouble(4));
                activity.setAvailable(cursor.getInt(5) == 1);
                activity.setCapacity(cursor.getInt(6));
                activity.setDuration(cursor.getString(7));
                activity.setGuideName(cursor.getString(8));
                activityList.add(activity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return activityList;
    }

    public Activity getActivity(int activityId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACTIVITY, null, KEY_ID + "=?",
                new String[]{String.valueOf(activityId)}, null, null, null);
        Activity activity = null;
        if (cursor.moveToFirst()) {
            activity = new Activity(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getInt(5) == 1,
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
        }
        cursor.close();
        db.close();
        return activity;
    }

    public int getActivityBookingsCount(int activityId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_ACTIVITY_BOOKING +
                " WHERE " + KEY_ACTIVITY_ID + " = ? AND " +
                KEY_BOOKING_DATE + " = ? AND " + KEY_STATUS + " = 'Confirmed'";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(activityId), date});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public boolean addActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, activity.getName());
        values.put(KEY_ACTIVITY_TYPE, activity.getType());
        values.put(KEY_DESCRIPTION, activity.getDescription());
        values.put(KEY_PRICE, activity.getPrice());
        values.put(KEY_AVAILABLE, activity.isAvailable() ? 1 : 0);
        values.put(KEY_CAPACITY, activity.getCapacity());
        values.put(KEY_DURATION, activity.getDuration());
        values.put(KEY_GUIDE_NAME, activity.getGuideName());
        long result = db.insert(TABLE_ACTIVITY, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateActivity(Activity activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, activity.getName());
        values.put(KEY_ACTIVITY_TYPE, activity.getType());
        values.put(KEY_DESCRIPTION, activity.getDescription());
        values.put(KEY_PRICE, activity.getPrice());
        values.put(KEY_AVAILABLE, activity.isAvailable() ? 1 : 0);
        values.put(KEY_CAPACITY, activity.getCapacity());
        values.put(KEY_DURATION, activity.getDuration());
        values.put(KEY_GUIDE_NAME, activity.getGuideName());
        int result = db.update(TABLE_ACTIVITY, values, KEY_ID + "=?", new String[]{String.valueOf(activity.getId())});
        db.close();
        return result > 0;
    }

    public boolean deleteActivity(int activityId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ACTIVITY, KEY_ID + "=?", new String[]{String.valueOf(activityId)});
        db.close();
        return result > 0;
    }

    public long bookRoom(int userId, int roomId, String checkIn, String checkOut, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_ROOM_ID, roomId);
        values.put(KEY_CHECK_IN, checkIn);
        values.put(KEY_CHECK_OUT, checkOut);
        values.put(KEY_TOTAL_PRICE, totalPrice);
        values.put(KEY_STATUS, "Confirmed");
        long result = db.insert(TABLE_BOOKING, null, values);
        if (result != -1) {
            Room room = getRoom(roomId);
            String roomName = (room != null) ? room.getName() : "Room";
            createNotification(userId, "Booking Confirmed",
                    "Your " + roomName + " booking from " + checkIn + " to " + checkOut + " is confirmed!");
        }
        db.close();
        return result;
    }

    public long bookActivity(int userId, int activityId, String bookingDate, int participants) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_ACTIVITY_ID, activityId);
        values.put(KEY_BOOKING_DATE, bookingDate);
        values.put(KEY_PARTICIPANTS, participants);
        values.put(KEY_STATUS, "Confirmed");
        long result = db.insert(TABLE_ACTIVITY_BOOKING, null, values);
        if (result != -1) {
            Activity activity = getActivity(activityId);
            String activityName = (activity != null) ? activity.getName() : "Activity";
            createNotification(userId, "Activity Booked",
                    "Your " + activityName + " on " + bookingDate + " is confirmed!");
        }
        db.close();
        return result;
    }

    public List<RoomBooking> getUserRoomBookings(int userId) {
        List<RoomBooking> bookings = new ArrayList<>();
        String query = "SELECT b.*, r." + KEY_NAME + " as room_name, r." + KEY_PRICE +
                " FROM " + TABLE_BOOKING + " b " +
                "JOIN " + TABLE_ROOM + " r ON b." + KEY_ROOM_ID + "=r." + KEY_ID +
                " WHERE b." + KEY_USER_ID + "=" + userId +
                " ORDER BY b." + KEY_CHECK_IN + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                RoomBooking booking = new RoomBooking(
                        cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), cursor.getDouble(5),
                        cursor.getString(6), cursor.getString(7), cursor.getDouble(8)
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    public List<ActivityBooking> getUserActivityBookings(int userId) {
        List<ActivityBooking> bookings = new ArrayList<>();
        String query = "SELECT ab.*, a." + KEY_NAME + " as activity_name, a." + KEY_PRICE +
                " FROM " + TABLE_ACTIVITY_BOOKING + " ab " +
                "JOIN " + TABLE_ACTIVITY + " a ON ab." + KEY_ACTIVITY_ID + "=a." + KEY_ID +
                " WHERE ab." + KEY_USER_ID + "=" + userId +
                " ORDER BY ab." + KEY_BOOKING_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ActivityBooking booking = new ActivityBooking(
                        cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getString(5),
                        cursor.getString(6), cursor.getDouble(7)
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    public List<RoomBooking> getAllRoomBookings() {
        List<RoomBooking> bookings = new ArrayList<>();
        String query = "SELECT b.*, r." + KEY_NAME + " as room_name, r." + KEY_PRICE +
                " FROM " + TABLE_BOOKING + " b " +
                "JOIN " + TABLE_ROOM + " r ON b." + KEY_ROOM_ID + "=r." + KEY_ID +
                " ORDER BY b." + KEY_CHECK_IN + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                RoomBooking booking = new RoomBooking(
                        cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getString(4), cursor.getDouble(5),
                        cursor.getString(6), cursor.getString(7), cursor.getDouble(8)
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    public List<ActivityBooking> getAllActivityBookings() {
        List<ActivityBooking> bookings = new ArrayList<>();
        String query = "SELECT ab.*, a." + KEY_NAME + " as activity_name, a." + KEY_PRICE +
                " FROM " + TABLE_ACTIVITY_BOOKING + " ab " +
                "JOIN " + TABLE_ACTIVITY + " a ON ab." + KEY_ACTIVITY_ID + "=a." + KEY_ID +
                " ORDER BY ab." + KEY_BOOKING_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ActivityBooking booking = new ActivityBooking(
                        cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getString(5),
                        cursor.getString(6), cursor.getDouble(7)
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    public void createNotification(int userId, String title, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_NAME, title);
        values.put(KEY_DESCRIPTION, message);
        values.put(KEY_DATE, getCurrentDate());
        values.put("is_read", 0);
        db.insert(TABLE_NOTIFICATION, null, values);
        db.close();
    }

    public List<Notification> getUserNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + KEY_USER_ID + " = ? ORDER BY " + KEY_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                notifications.add(new Notification(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(5) == 1
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notifications;
    }

    public void markNotificationAsRead(int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_read", 1);
        db.update(TABLE_NOTIFICATION, values, KEY_ID + "=?", new String[]{String.valueOf(notificationId)});
        db.close();
    }

    public void sendNotificationToAllUsers(String title, String message) {
        List<User> users = getAllUsers();
        for (User user : users) {
            createNotification(user.getId(), title, message);
        }
    }

    public boolean deleteNotification(int notificationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTIFICATION, KEY_ID + " = ?", new String[]{String.valueOf(notificationId)});
        db.close();
        return result > 0;
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " ORDER BY " + KEY_DATE + " DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5) == 1
                );
                notifications.add(notification);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notifications;
    }

    public boolean deleteNotificationForUser(int notificationId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTIFICATION, KEY_ID + " = ? AND " + KEY_USER_ID + " = ?", new String[]{String.valueOf(notificationId), String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    // NEW METHOD: Delete specific Room Booking
    public boolean deleteRoomBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKING, KEY_ID + "=?", new String[]{String.valueOf(bookingId)});
        db.close();
        return result > 0;
    }

    // NEW METHOD: Delete specific Activity Booking
    public boolean deleteActivityBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ACTIVITY_BOOKING, KEY_ID + "=?", new String[]{String.valueOf(bookingId)});
        db.close();
        return result > 0;
    }

    public List<ActivityBooking> getAllConfirmedActivityBookings() {
        List<ActivityBooking> bookings = new ArrayList<>();
        String query = "SELECT ab.*, a." + KEY_NAME + " as activity_name, a." + KEY_PRICE +
                " FROM " + TABLE_ACTIVITY_BOOKING + " ab " +
                "JOIN " + TABLE_ACTIVITY + " a ON ab." + KEY_ACTIVITY_ID + "=a." + KEY_ID +
                " WHERE ab." + KEY_STATUS + " = 'Confirmed' " +
                " ORDER BY ab." + KEY_BOOKING_DATE + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ActivityBooking booking = new ActivityBooking(
                        cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getString(3), cursor.getInt(4), cursor.getString(5),
                        cursor.getString(6), cursor.getDouble(7)
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    private String getCurrentDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
    }
}