package com.dicoding.tugas_akhir.data.mapper

import com.dicoding.tugas_akhir.data.remote.response.BookingResponse
import com.dicoding.tugas_akhir.data.remote.response.PassengerResponse
import com.dicoding.tugas_akhir.data.remote.response.ShipScheduleResponse
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.Passenger
import com.dicoding.tugas_akhir.domain.model.ShipSchedule

object DataMapper {

    fun mapScheduleResponseToDomain(input: ShipScheduleResponse): ShipSchedule {
        return ShipSchedule(
            id = input.id,
            shipName = input.shipName,
            shipCode = input.shipCode,
            origin = input.origin,
            destination = input.destination,
            departureDate = input.departureDate,
            departureTime = input.departureTime,
            arrivalDate = input.arrivalDate,
            arrivalTime = input.arrivalTime,
            duration = input.duration,
            economyPrice = input.economyPrice,
            businessPrice = input.businessPrice,
            firstClassPrice = input.firstClassPrice,
            quota = input.quota,
            status = input.status,
            facilities = input.facilities,
            description = input.description,
            canRefund = input.canRefund,
            canReschedule = input.canReschedule,
        )
    }

    fun mapScheduleResponsesToDomain(input: List<ShipScheduleResponse>): List<ShipSchedule> {
        return input.map { mapScheduleResponseToDomain(it) }
    }

    fun mapPassengerResponseToDomain(input: PassengerResponse): Passenger {
        return Passenger(
            id = input.id,
            fullName = input.fullName,
            nik = input.nik,
            phoneNumber = input.phoneNumber,
            birthDate = input.birthDate,
            gender = input.gender,
        )
    }

    fun mapBookingResponseToDomain(input: BookingResponse): Booking {
        return Booking(
            id = input.id,
            scheduleId = input.scheduleId,
            shipName = input.shipName,
            origin = input.origin,
            destination = input.destination,
            departureDate = input.departureDate,
            departureTime = input.departureTime,
            ticketClassName = input.ticketClassName,
            ticketPrice = input.ticketPrice,
            passengerCount = input.passengerCount,
            passengers = input.passengers.map { mapPassengerResponseToDomain(it) },
            adminFee = input.adminFee,
            totalPrice = input.totalPrice,
            status = input.status,
            createdAt = input.createdAt,
            paymentDeadline = input.paymentDeadline,
        )
    }
}