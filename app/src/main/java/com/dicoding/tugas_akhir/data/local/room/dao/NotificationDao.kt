package com.dicoding.tugas_akhir.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.tugas_akhir.data.local.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY createdAtMillis DESC")
    fun getNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadNotificationCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(
        notification: NotificationEntity,
    ): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(
        notifications: List<NotificationEntity>,
    ): List<Long>

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(
        id: String,
    ): Int

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead(): Int

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(
        id: String,
    ): Int

    @Query("DELETE FROM notifications")
    suspend fun clearNotifications(): Int
}