package com.rroperations.utils

import com.rroperations.models.Train
import com.rroperations.models.DestinationsOrder
import com.rroperations.models.ReceiversOrder
import jakarta.inject.Singleton

@Singleton
class TrainComparator(

    private val destinationsOrder : DestinationsOrder,
    private val receiversOrder: ReceiversOrder) {

    fun sortTrains(trains: ArrayList<Train>): List<Train> {

        val destinations = destinationsOrder.destinations
        val receivers = receiversOrder.receivers

        var (trainExpectedValues, trainUnexpectedValues) = trains.partition {
            destinations.containsKey(it.destination) && receivers.containsKey(it.receiver)
        }

        val destinationComparator = Comparator { o1: Train, o2: Train ->

            if (!destinations.containsKey(o1.destination) || !receivers.containsKey(o1.receiver)){
                return@Comparator 1
            } else if (!destinations.containsKey(o2.destination) || !receivers.containsKey(o2.receiver)){
                return@Comparator -1
            }

            return@Comparator if (o1.destination == o2.destination) receivers[o1.receiver]!! - receivers[o2.receiver]!!
            else destinations[o1.destination]!! - destinations[o2.destination]!!
        }

        trainExpectedValues = trainExpectedValues.sortedWith(destinationComparator)

        return trainExpectedValues + trainUnexpectedValues
    }
}