//Ari Rubin 315528547 & Simcha Podolsky 311215149
//Simcha Podolsky 311215149 & Ari Rubin 315528547
package com.example.socialdeliverysystem.Utils;

import com.example.socialdeliverysystem.Entites.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDBManager {

    public static DatabaseReference databaseReference;
    public static DatabaseReference usersRef;
    public static DatabaseReference newPackagesRef;
    public static DatabaseReference oldPackagesRef;
    private static Person currentUserPerson;
    private static FirebaseUser currentUser;

    public static FirebaseUser getCurrentUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser;
    }

    public static void setCurrentUserPerson(Person user) {
        currentUserPerson = user;
    }

    public static Person getCurrentUserPerson() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ? currentUserPerson : null;
    }


    static {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usersRef = databaseReference.child("users").getRef();
        newPackagesRef = databaseReference.child("packages/newPackages").getRef();
        oldPackagesRef = databaseReference.child("packages/oldPackages").getRef();
    }

    public static void addUserToFirebase(final Person users) {
        usersRef.child(users.getPhoneNumber()).setValue(users);
    }

}
