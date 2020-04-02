package sk.itsovy.strausz.project;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Mongo {


    private MongoClient getConnection() {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
        return new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

    }

    public void insertOne() {


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter fullname: ");
        String fullName = scanner.nextLine();
        System.out.println("Enter age: ");
        int age = scanner.nextInt();
        System.out.println("Enter gender:");
        String gender = scanner.nextLine();

        scanner.next();

        MongoClient mongo = getConnection();
        MongoDatabase db = mongo.getDatabase("company");
        MongoCollection<Document> collection = db.getCollection("users");


        Document users = new Document("name", fullName)
                .append("age", age)
                .append("gender", gender);

        collection.insertOne(users);
        scanner.close();
        mongo.close();
    }

    public void insertMany() {

        MongoClient mongo = getConnection();
        MongoDatabase db = mongo.getDatabase("company");
        MongoCollection<Document> collection = db.getCollection("users");

        Document user = new Document("name", "Karol Veľký")
                .append("age", 38)
                .append("gender", "M");

        Document user2 = new Document("name", "Gabriel Malý")
                .append("age", 25)
                .append("gender", "M");

        Document user3 = new Document("name", "Karolína Sladká")
                .append("age", 39)
                .append("gender", "F");

        Document user4 = new Document("name", "Mária Hrubá")
                .append("age", 98)
                .append("gender", "F");

        List<Document> allUsers = new ArrayList<>();
        allUsers.add(user);
        allUsers.add(user2);
        allUsers.add(user3);
        allUsers.add(user4);

        collection.insertMany(allUsers);


    }

    public void delete() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter user to delete: ");
        String fullname = scanner.nextLine();

        MongoClient mongo = getConnection();
        MongoDatabase db = mongo.getDatabase("company");
        MongoCollection<Document> collection = db.getCollection("users");

        BasicDBObject theQuery = new BasicDBObject();
        theQuery.put("name", fullname);
        DeleteResult result = collection.deleteOne(theQuery);
        System.out.println("The Numbers of Deleted Document(s) : " + result.getDeletedCount());

    }

    public void update() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username to update: ");
        String userName = scanner.nextLine();
        System.out.println("Enter new name: ");
        String fullname = scanner.nextLine();
        System.out.println("Enter new age: ");
        int age = scanner.nextInt();


        MongoClient mongo = getConnection();
        MongoDatabase db = mongo.getDatabase("company");
        MongoCollection<Document> collection = db.getCollection("users");


        BasicDBObject query = new BasicDBObject();
        query.put("name", userName);


        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("name", fullname);
        newDocument.put("age", age);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

        UpdateResult result = collection.updateOne(query, updateObject);
    }


    public void printDB() {
        MongoClient mongo = getConnection();
        MongoDatabase db = mongo.getDatabase("company");
        MongoCollection<Document> collection = db.getCollection("users");

        FindIterable<Document> findIterable = collection.find(new Document());

        for (Document s : findIterable) {
            System.out.println(s.toJson());
        }
    }
}
