package no.nav.familie.ba.infotrygd.replika.security

enum class Rolle {
    FORVALTER,
    APPLICATION,
    SAKSBEHANDLER,
    VEILEDER,
    BESLUTTER,
    ;

    fun authority(): String = "ROLE_$name"
}
