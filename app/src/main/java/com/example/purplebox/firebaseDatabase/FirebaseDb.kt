package com.example.purplebox.firebaseDatabase

import com.example.purplebox.model.CartProduct
import com.example.purplebox.model.User
import com.example.purplebox.util.Constants.Companion.BEST_DEALS
import com.example.purplebox.util.Constants.Companion.CART_COLLECTION
import com.example.purplebox.util.Constants.Companion.CATEGORY
import com.example.purplebox.util.Constants.Companion.ID
import com.example.purplebox.util.Constants.Companion.PRODUCTS_COLLECTION
import com.example.purplebox.util.Constants.Companion.QUANTITY
import com.example.purplebox.util.Constants.Companion.SIZE
import com.example.purplebox.util.Constants.Companion.USERS_COLLECTION
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseDb {
    private val usersCollectionRef = Firebase.firestore.collection(USERS_COLLECTION)
    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)
    private val firebaseStorage = Firebase.storage.reference

    val userUid = FirebaseAuth.getInstance().currentUser?.uid

    private val userCartCollection = userUid?.let {
        Firebase.firestore.collection(USERS_COLLECTION).document(it).collection(CART_COLLECTION)
    }

    private val firebaseAuth = Firebase.auth

    fun createNewUser(
        email: String, password: String
    ) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun saveUserInformation(
        userUid: String,
        user: User
    ) = usersCollectionRef.document(userUid).set(user)

    fun loginUser(
        email: String,
        password: String
    ) = firebaseAuth.signInWithEmailAndPassword(email, password)

/*    fun getClothesProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CLOTHES).limit(pagingPage).get()*/

    fun getBestDealsProducts(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, BEST_DEALS).limit(pagingPage).get()

    fun getHomeProducts(pagingPage: Long) =
        productsCollection.limit(pagingPage).get()

    //add order by orders
/*    fun getMostOrderedCupboard(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .orderBy(ORDERS, Query.Direction.DESCENDING).limit(pagingPage).get()

    fun getCupboards(pagingPage: Long) =
        productsCollection.whereEqualTo(CATEGORY, CUPBOARD_CATEGORY).limit(pagingPage)
            .limit(pagingPage).get()*/

    fun addProductToCart(product: CartProduct) = userCartCollection?.document()!!.set(product)

    fun getProductInCart(product: CartProduct) = userCartCollection!!
        .whereEqualTo(ID, product.id)
        .whereEqualTo(SIZE, product.size).get()

    fun increaseProductQuantity(documentId: String): Task<Transaction> {
        val document = userCartCollection!!.document(documentId)
        return Firebase.firestore.runTransaction { transaction ->
            val productBefore = transaction.get(document)
            var quantity = productBefore.getLong(QUANTITY)
            quantity = quantity!! + 1
            transaction.update(document, QUANTITY, quantity)
        }

    }

    fun getUser() = usersCollectionRef
        .document(FirebaseAuth.getInstance().currentUser!!.uid)

    /*fun productRef(product: String) = productsCollectionRef*/

    /*fun checkUserByEmail(email: String, onResult: (String?, Boolean?) -> Unit) {
        usersCollectionRef.whereEqualTo("email", email).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObjects(User::class.java)
                    if (user.isEmpty())
                        onResult(null, false)
                    else
                        onResult(null, true)
                } else
                    onResult(it.exception.toString(), null)
            }
    }*/

    fun logout() = Firebase.auth.signOut()

}