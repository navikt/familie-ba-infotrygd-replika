package no.nav.familie.ba.infotrygd.replika

private var current: Long = 1

fun nextId(): Long = current++
