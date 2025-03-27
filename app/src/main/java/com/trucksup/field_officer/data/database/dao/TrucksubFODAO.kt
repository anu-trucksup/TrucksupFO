package com.trucksup.field_officer.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trucksup.field_officer.data.database.model.TranslationTableModel

@Dao
interface TrucksubFODAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTranslations(translations: TranslationTableModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTranslations(translations: TranslationTableModel)


    @Query("select * from translations_table where languageCode = :langCode")
    fun getTranslations(langCode: String) : TranslationTableModel


    @Query("select * from translations_table")
    fun getAllTranslations() : List<TranslationTableModel>

    @Delete
    fun deleteTranslations(translations: TranslationTableModel)

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAirports(airports: List<AirportModel>)*/

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWishListItem(wishListProduct: WishListProduct)

    @Query("select * from wish_list_table")
    fun getWishLists() : List<WishListProduct>
    @Query("select * from wish_list_table WHERE product_id=:id")
    fun getWishListItem(id: Int) : WishListProduct

    @Delete
    fun deleteWishListItem(wishListProduct: WishListProduct)

    @Query("DELETE FROM wish_list_table")
    fun deleteAllWishListItem()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWishListEsimItem(wishListEsim: WishListEsim)
    @Query("select * from wish_list_esim_table")
    fun getEsimWishLists() : List<WishListEsim>

    @Query("select * from wish_list_esim_table WHERE id=:id")
    fun getEsimWishListItem(id: String) : WishListEsim

    @Delete
    fun deleteEsimWishListItem(wishListEsim: WishListEsim)

    @Query("DELETE FROM wish_list_esim_table")
    fun deleteAllEsimWishListItem()*/

}