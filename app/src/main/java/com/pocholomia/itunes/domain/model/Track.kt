package com.pocholomia.itunes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pocholomia.itunes.ui.utils.toDate
import com.pocholomia.itunes.ui.utils.toReadableString
import com.squareup.moshi.Json
import java.util.*
import java.util.concurrent.TimeUnit

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey @field:Json(name = "trackId") val id: Long,
    @field:Json(name = "trackName") val name: String?,
    @field:Json(name = "primaryGenreName") val primaryGenreName: String,
    @field:Json(name = "releaseDate") val releaseDate: String,
    @field:Json(name = "artworkUrl100") val artworkUrl: String,
    @field:Json(name = "trackPrice") val trackPrice: Double,
    @field:Json(name = "trackRentalPrice") val trackRentalPrice: Double,
    @field:Json(name = "currency") val currency: String,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "longDescription") val longDescription: String? = null,
    @field:Json(name = "trackTimeMillis") val trackTimeMillis: Long,
    @field:Json(name = "sortOrder") val sortOrder: Long
) {

    val getSmallArtworkUrl: String
        get() = artworkUrl.replace("100x100", "150x150")

    val getBigArtworkUrl: String
        get() = artworkUrl.replace("100x100", "300x300")

    val priceDisplay: String
        get() {
            val currency: Currency = Currency.getInstance(this.currency)
            val symbol: String = currency.symbol
            val trackPrice = "Buy: $symbol ${this.trackPrice}"
            val rentalPrice = "Rent: $symbol ${this.trackRentalPrice}"
            return "$trackPrice / $rentalPrice"
        }

    val releaseDateDisplay: String
        get() = releaseDate
            .toDate("yyyy-MM-dd'T'HH:mm:ssZ")
            ?.toReadableString("MMMM dd, yyyy") ?: ""

    val descriptionDisplay: String
        get() = longDescription ?: description ?: "No Description Available"

    val runningTime: String
        get() = String.format(
            "%01d hr %02d min",
            TimeUnit.MILLISECONDS.toHours(trackTimeMillis),
            TimeUnit.MILLISECONDS.toMinutes(trackTimeMillis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(trackTimeMillis)
            )
        )

}