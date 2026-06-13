package com.dicoding.tugas_akhir.data.mapper

import com.dicoding.tugas_akhir.data.local.room.entity.BookingEntity
import com.dicoding.tugas_akhir.data.local.room.entity.BookingWithPassengers
import com.dicoding.tugas_akhir.data.local.room.entity.PassengerEntity
import com.dicoding.tugas_akhir.data.local.room.entity.PaymentEntity
import com.dicoding.tugas_akhir.data.remote.response.BookingResponse
import com.dicoding.tugas_akhir.data.remote.response.ETicketResponse
import com.dicoding.tugas_akhir.data.remote.response.PassengerResponse
import com.dicoding.tugas_akhir.data.remote.response.PaymentMethodResponse
import com.dicoding.tugas_akhir.data.remote.response.PaymentResponse
import com.dicoding.tugas_akhir.data.remote.response.ShipScheduleResponse
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.ETicket
import com.dicoding.tugas_akhir.domain.model.Passenger
import com.dicoding.tugas_akhir.domain.model.Payment
import com.dicoding.tugas_akhir.domain.model.PaymentMethod
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

    fun mapPaymentMethodResponseToDomain(input: PaymentMethodResponse): PaymentMethod {
        return PaymentMethod(
            id = input.id,
            name = input.name,
            description = input.description,
        )
    }

    fun mapPaymentMethodResponsesToDomain(input: List<PaymentMethodResponse>): List<PaymentMethod> {
        return input.map { mapPaymentMethodResponseToDomain(it) }
    }

    fun mapPaymentResponseToDomain(input: PaymentResponse): Payment {
        return Payment(
            id = input.id,
            bookingId = input.bookingId,
            paymentMethodId = input.paymentMethodId,
            paymentMethodName = input.paymentMethodName,
            totalPrice = input.totalPrice,
            paymentCode = input.paymentCode,
            status = input.status,
            expiredIn = input.expiredIn,
            instructions = input.instructions,
            createdAt = input.createdAt,
        )
    }

    fun mapETicketResponseToDomain(input: ETicketResponse): ETicket {
        return ETicket(
            bookingId = input.bookingId,
            bookingCode = input.bookingCode,
            paymentId = input.paymentId,
            shipName = input.shipName,
            origin = input.origin,
            destination = input.destination,
            departureDate = input.departureDate,
            departureTime = input.departureTime,
            ticketClassName = input.ticketClassName,
            passengers = input.passengers.map { mapPassengerResponseToDomain(it) },
            status = input.status,
            qrCode = input.qrCode,
            issuedAt = input.issuedAt,
            terminal = input.terminal,
            gate = input.gate,
            note = input.note,
        )
    }

    private const val INSTRUCTION_SEPARATOR = "|||"

    fun mapBookingDomainToEntity(input: Booking): BookingEntity {
        return BookingEntity(
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
            adminFee = input.adminFee,
            totalPrice = input.totalPrice,
            status = input.status,
            createdAt = input.createdAt,
            paymentDeadline = input.paymentDeadline,
            createdAtMillis = System.currentTimeMillis(),
        )
    }

    fun mapPassengerDomainToEntity(
        input: Passenger,
        bookingId: String,
        passengerOrder: Int,
    ): PassengerEntity {
        return PassengerEntity(
            id = input.id,
            bookingId = bookingId,
            passengerOrder = passengerOrder,
            fullName = input.fullName,
            nik = input.nik,
            phoneNumber = input.phoneNumber,
            birthDate = input.birthDate,
            gender = input.gender,
        )
    }

    fun mapPassengerEntityToDomain(input: PassengerEntity): Passenger {
        return Passenger(
            id = input.id,
            fullName = input.fullName,
            nik = input.nik,
            phoneNumber = input.phoneNumber,
            birthDate = input.birthDate,
            gender = input.gender,
        )
    }

    fun mapBookingWithPassengersToDomain(input: BookingWithPassengers): Booking {
        return Booking(
            id = input.booking.id,
            scheduleId = input.booking.scheduleId,
            shipName = input.booking.shipName,
            origin = input.booking.origin,
            destination = input.booking.destination,
            departureDate = input.booking.departureDate,
            departureTime = input.booking.departureTime,
            ticketClassName = input.booking.ticketClassName,
            ticketPrice = input.booking.ticketPrice,
            passengerCount = input.booking.passengerCount,
            passengers = input.passengers
                .sortedBy { it.passengerOrder }
                .map { mapPassengerEntityToDomain(it) },
            adminFee = input.booking.adminFee,
            totalPrice = input.booking.totalPrice,
            status = input.booking.status,
            createdAt = input.booking.createdAt,
            paymentDeadline = input.booking.paymentDeadline,
        )
    }

    fun mapPaymentDomainToEntity(input: Payment): PaymentEntity {
        return PaymentEntity(
            id = input.id,
            bookingId = input.bookingId,
            paymentMethodId = input.paymentMethodId,
            paymentMethodName = input.paymentMethodName,
            totalPrice = input.totalPrice,
            paymentCode = input.paymentCode,
            status = input.status,
            expiredIn = input.expiredIn,
            instructions = input.instructions.joinToString(INSTRUCTION_SEPARATOR),
            createdAt = input.createdAt,
            createdAtMillis = System.currentTimeMillis(),
        )
    }

    fun mapPaymentEntityToDomain(input: PaymentEntity): Payment {
        return Payment(
            id = input.id,
            bookingId = input.bookingId,
            paymentMethodId = input.paymentMethodId,
            paymentMethodName = input.paymentMethodName,
            totalPrice = input.totalPrice,
            paymentCode = input.paymentCode,
            status = input.status,
            expiredIn = input.expiredIn,
            instructions = input.instructions
                .split(INSTRUCTION_SEPARATOR)
                .filter { it.isNotBlank() },
            createdAt = input.createdAt,
        )
    }
}