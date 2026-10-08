# Kodeverk i replika

Kodene som står i kodefeltene i [tabellene](tabeller.md).

**Kilder:**
- «Brukerhåndbok Infotrygd 06 Barnetrygd», Rikstrygdeverket, 1. mai 1997 (merket *håndbok*)
- «Infotrygd Brukerhåndbok 14 Saksbehandlingsrutinen», Arbeids- og velferdsdirektoratet, 2005, sist endret desember 2016 (merket *SB-håndbok*). Gjelder saksregisteret (`sa_`-tabellene) for alle ytelser, ikke barnetrygd spesielt
- Confluence-siden «Databaser» under Modernisering av IKT i Nav (merket *Confluence*)
- koden i dette repoet (merket *kode*)

Håndboken beskriver Infotrygd slik systemet var i 1997. Koder kan ha kommet til eller endret betydning senere. Når håndboken og koden er uenige, gjelder koden.

## Klassifisering (valg og undervalg)

Feltene `sa_sak_10.s10_valg` og `s10_undervalg` beskriver hvilken type barnetrygd saken gjelder. I DB2 står samme klassifisering i `t_stonadsklasse`: nivå `02` er valget og nivå `03` er undervalget.

| Valg | Undervalg | Betydning | Kilde |
|---|---|---|---|
| `OR` | `OS` | Ordinær barnetrygd, ordinær stønad | håndbok |
| `OR` | `IB` | Ordinær barnetrygd, institusjonsbarn. Beregnes alltid etter satsen for første barn | håndbok |
| `OR` | `OI` | Ordinær barnetrygd, hjemmeværende barn med søsken i institusjon. Beregnes etter satsen fra og med andre barn | håndbok |
| `OR` | `MB` | Ordinær barnetrygd, manuell beregning | håndbok |
| `OR` | `MD` | Ordinær barnetrygd, manuell beregning ved delt bosted | Confluence, kode (`BarnetrygdService.MANUELL_BEREGNING_DELT_BOSTED`) |
| `OR` | `EU` | Ordinær barnetrygd, EU-sak der Norge er primærland | Confluence |
| `OR` | `ME` | Ordinær barnetrygd, manuell beregning i EU-sak | Confluence |
| `UT` | `EF` | Utvidet barnetrygd, enslig forsørger | håndbok |
| `UT` | `EI` | Utvidet barnetrygd, enslig forsørger med hjemmeværende barn og søsken i institusjon | håndbok |
| `UT` | `SY` | Utvidet barnetrygd, sykt barn | håndbok |
| `UT` | `SI` | Utvidet barnetrygd, sykt barn med søsken i institusjon | håndbok |
| `UT` | `MB` | Utvidet barnetrygd, manuell beregning | håndbok |
| `UT` | `MD` | Utvidet barnetrygd, manuell beregning ved delt bosted | Confluence |
| `UT` | `EU` | Utvidet barnetrygd, EU-sak der Norge er primærland | Confluence |
| `UT` | `ME` | Utvidet barnetrygd, manuell beregning i EU-sak | Confluence |

Kodene erstattet de gamle valgkodene under kapittel 30 (`010`–`027`) da ny folketrygdlov trådte i kraft 5. mai 1997.

I håndboken heter klassifiseringen «statuskode». Den er noe annet enn behandlingsstatusen i `sa_status_15.s15_status` (se `SakStatus`).

I 1997 skulle alle EØS-saker registreres med `MB` (håndbok). Senere kom egne undervalg for EU-saker (`EU`, `ME`).

Kilden har to kolonner for undervalg (`S10_UNDERVALG_1` og `S10_UNDERVALG_2`). Replika har bare `s10_undervalg`.

## Stønadsstatus (`b20_status`)

`ba_stoenad_20.b20_status` er den samme klassifiseringen som i saken, men med færre detaljer. Sammen med `b20_eos` gir den valg og undervalg i `sa_sak_10`.

| `b20_status` | `b20_eos` | Betydning | Valg | Undervalg |
|---|---|---|---|---|
| `01` | blank | Ordinær stønad | `OR` | `OS` |
| `01` | `J` | EU-sak, Norge primærland | `OR` | `EU` |
| `04` | blank | Institusjonsbarn | `OR` | `IB` |
| `02` | blank | Enslig forsørger | `UT` | `EF` |
| `02` | `J` | EU-sak, Norge primærland | `UT` | `EU` |
| `00` | blank | Manuell beregning, eventuelt ved delt bosted | `OR` eller `UT` | `MB` eller `MD` |
| `00` | `J` | Manuell beregning i EU-sak | `OR` eller `UT` | `ME` |

`b20_status = '00'` skiller ikke mellom ordinær og utvidet, eller mellom `MB`, `MD` og `ME`. For å finne den nøyaktige klassifiseringen må saken slås opp i `sa_sak_10` med `b20_blokk` og `b20_sak_nr`. Da trengs ikke `b20_eos`, siden `ME` står i saken.

Håndboken bruker også status `00` for EØS-differanseutbetaling.

Kilde: Confluence-siden «Databaser» (Modernisering av IKT i Nav), etter beskrivelse fra Tore Hågensen.

## Sakstype

`sa_sak_10.s10_type`.

| Kode | Betydning | Kilde |
|---|---|---|
| `S` | Søknad | håndbok |
| `R` | Revurdering | håndbok |
| `K` | Klage | håndbok |
| `A` | Anke | håndbok |
| `AT` | Anke tilbakebetaling | håndbok |
| `I` | Informasjonssak | håndbok |
| `FS` | Forespørsel, for eksempel den årlige egenerklæringen for utvidet barnetrygd | håndbok |
| `SE` | Søknad om ettergivelse av tilbakebetaling | håndbok |
| `T` | Tilbakebetalingssak (feilutbetaling) | håndbok, SB-håndbok |
| `TA` | Feilutbetalingssak. Nevnt sammen med `T`, nøyaktig betydning ukjent | SB-håndbok |
| `KE` | Klage på ettergivelse | håndbok |
| `FL` | Flyttesak | kode (kommentar i `SakRepository.findBarnetrygdsakerByBarnFnr`) |
| `AS` | Automatisk stønad | kode (kommentar i `SakRepository.findBarnetrygdsakerByBarnFnr`) |

Kommentarene i `SakRepository` beskriver typene i spørringen, men de stemmer ikke alltid:

- I `findBarnetrygdsakerByBarnFnr` stemmer kommentaren med listen `('S', 'R', 'K', 'A', 'FL', 'AS')`.
- I `findBarnetrygdsakerByFnr` er kommentaren kopiert fra metoden over og stemmer ikke med listen `('S', 'R', 'FL', 'AS', 'FS')`. Kommentaren nevner klage og anke, men `K` og `A` er ikke med i listen. Den nevner heller ikke `FS`.

Beskrivelsen av `FS` som forespørsel kommer fra håndboken.

## Resultat

`sa_sak_10.s10_resultat`. Hvilke resultater som er gyldige, avhenger av klassifiseringen.

| Kode | Betydning | Kilde |
|---|---|---|
| `I` | Innvilget | håndbok |
| `A` | Avslag | håndbok |
| `O` | Opphør | håndbok |
| `H` | Henlagt | håndbok |
| `SB` | Sak i bero | håndbok |
| `Ø` | Økning | håndbok |
| `FI` | Fortsatt innvilget | håndbok |
| `R` | Redusert | håndbok |
| `RO` | Redusert til ordinær stønad | håndbok |
| `DI` | Delvis innvilget | håndbok |
| `HB` | Henlagt/bortfalt. Brukes når saken er feilregistrert, for eksempel på feil person | SB-håndbok |
| `FB` | Ferdig behandlet | teamet |

`SB` (sak i bero) skal bare brukes når retten ikke har oppstått ennå. Saken får da nivå `TK` og oppfølging om 13 uker (*SB-håndbok*).

`t_vedtak.kode_resultat = 'HB'` betyr også henlagt/bortfalt (*kode*, `VedtakRepository`).

## Behandlingsstatus

`sa_status_15.s15_status`, i koden `SakStatus`. Viser hvor saken er i saksbehandlingen.

Den nyere håndboken beskriver flyten ved oversendelse til klageinstansen:

1. Saken sendes: status blir `ST` (sendt til), og behandlende enhet blir klageinstansen, for eksempel `K02`.
2. Saken kommer tilbake: status blir `RT` (retur til), og behandlende enhet blir enheten som sendte saken.
3. Returdato registreres på hendelseslinjen: status blir `FB` (ferdig behandlet).

Kilde: SB-håndbok. De andre kodene står i `SakStatus`.

## Stønadstype for barn

`ba_barn_10.b10_stonads_type`. Viser barnets status i stønaden.

| Kode | Betydning |
|---|---|
| `IN` | Institusjonsbarn |
| `SY` | Sykt barn |
| `SK` | Særkullsbarn |
| blank | Ordinær utbetaling |
| `N` | Ingen utbetaling for barnet |
| `FJ` | Kommando i skjermbildet for å fjerne et feilregistrert, ufullstendig fødselsnummer. Skal ikke finnes i lagrede data |

Barn med `IN` eller `N` telles ikke med i `b20_ant_barn`.

Kilde: håndbok.

## Samboertype

`ba_stoenad_20.b20_samboertype`.

| Kode | Betydning |
|---|---|
| `F` | Samboere med bare fellesbarn |
| `S` | Samboere med bare særkullsbarn. Barna har stønadstype `SK` |
| `B` | Samboere med både felles- og særkullsbarn. Særkullsbarna har stønadstype `SK` |
| blank | Ikke samboer, for eksempel gift |

Kilde: håndbok.

## Tekstkode

`ba_stoenad_20.b20_tekstkode`. Standardteksten i vedtaksbrevet.

| Kode | Tekst |
|---|---|
| `01` | Ordinær |
| `02` | Ektefelle død |
| `03` | En av foreldrene død |
| `04` | Begge foreldrene døde |
| `05` | Skilt |
| `06` | Separert |
| `07` | Faktisk separasjon |
| `08` | Ugift mor |
| `09` | Ugift far |
| `10` | Ektefelle forsvunnet |
| `11` | Ektefelle tvunget fravær |
| `12` | Enslig |
| `13` | Sykt barn |
| `14` | Midlertidig separasjon |
| `17` | Barn i institusjon |
| `18` | Barn i institusjon (kombinert tekst) |
| `19`–`31` | Tekst `18` kombinert med tekst `02`–`14` |
| `33` | Omregnet fra utvidet til ordinær stønad |
| `35` | Delt utbetaling ved delt omsorg |
| `36` | Omregnet på grunn av barns død |
| `37` | Omregnet fordi barn har fylt 16 år |
| `38` | Omregnet til ordinær stønad på grunn av nytt fellesbarn |
| `40` | Omregnet fordi barn har fylt 3 år |
| `41` | Omregnet fordi barn har fylt 3 år eller 16 år |
| `42` | Omregnet, samboere med fellesbarn og særkullsbarn |
| `43` | Omregnet, samboere med bare særkullsbarn |
| `44` | Omregnet, forespørsel ikke besvart |
| `45` | Omregnet, barn fylt 1 år og 16 år |
| `46` | Omregnet, barn fylt 1 år og 3 år |
| `47` | Omregnet, barn fylt 1 år, 3 år og 16 år |
| `48` | Omregnet, barn fylt 1 år |
| `50` | Spørrebrev for samboere |
| `51` | Purring på spørrebrev for samboere |
| `99` | Vedtaksbrev med fritekst |

Kilde: håndbok.

## Opphørsgrunn

`ba_stoenad_20.b20_opphorsgrunn`.

| Kode | Betydning | Kilde |
|---|---|---|
| `5` | Migrert ut av Infotrygd, se forbeholdet under | teamet |

Kode `5` betyr migrert ut av Infotrygd når stønaden kan knyttes til en nyere sak eller stønad fra perioden da migreringen startet, omtrent fra og med 2021. Koden alene er ikke tilstrekkelig til å fastslå at stønaden er migrert.

Andre koder er ikke dokumentert.

## Brevkode på hendelse

`sa_hendelse_20.s20_tekstkode_1`. Brevkodene som ba-sak har brukt for å sjekke om et brev er sendt (`HendelseRepository.findHendelseByFnrInAndTekstKoderIn`). Infotrygd lagrer den korte koden. Navnet i parentes er enum-verdien i ba-sak.

| Kode | Betydning | Navn i ba-sak |
|---|---|---|
| `BA04` | Batch, opphør småbarnstillegg | `BREV_BATCH_OPPHØR_SMÅBARNSTILLLEGG` |
| `BA05` | Batch, innvilget småbarnstillegg | `BREV_BATCH_INNVILGET_SMÅBARNSTILLEGG` |
| `BA37` | Batch, omregning barn 18 år | `BREV_BATCH_OMREGNING_BARN_18_ÅR` |
| `BA18` | Batch, omregning barn 6 år | `BREV_BATCH_OMREGNING_BARN_6_ÅR` |
| `B001` | Manuell, opphør småbarnstillegg | `BREV_MANUELL_OPPHØR_SMÅBARNSTILLLEGG` |
| `B002` | Manuell, innvilget småbarnstillegg | `BREV_MANUELL_INNVILGET_SMÅBARNSTILLEGG` |
| `B003` | Manuell, omregning barn 18 år | `BREV_MANUELL_OMREGNING_BARN_18_ÅR` |
| `BA19` | Manuell, omregning barn 6 år | `BREV_MANUELL_OMREGNING_BARN_6_ÅR` |

Aksjonsdatoen (`s20_aksjonsdato_seq`) lagres invertert som `99999999 - yyyyMMdd`. En lavere verdi betyr en senere dato.

Kilde: ba-sak.

## Konto

Kontoene på utbetalingslinjene (`ba_utbetaling_30.b30_kontonr`). Kontoplanen har endret seg over tid. De tre- og sjusifrede kontoene øverst er fra håndboken (1997) og er utgått.

| Kontonummer | Betydning | Status | Kilde |
|---|---|---|---|
| `601` | Barnetrygd | Utgått (1997) | håndbok |
| `602` | Nord-Norge-tillegg | Utgått (1997) | håndbok |
| `603` | Småbarnstillegg | Utgått (1997) | håndbok |
| `0540006` | Returføring og korreksjon | Utgått (1997) | håndbok |
| `0540008` | A konto (forskudd) | Utgått (1997) | håndbok |
| `052xxxx` | Andre trekk, via kreditornummer | Utgått (1997) | håndbok |
| `06030000` | Småbarnstillegg (657 kr per måned) | Avviklet 1. august 2003 | teamet |
| `06040000` | Ekstra småbarnstillegg, i dag kalt småbarnstillegg (660 kr per måned). Gis til enslige forsørgere med utvidet barnetrygd, full overgangsstønad og barn under 3 år | Gammelt nummer, men brukt i koden | teamet, kode |
| `8000010` | Barnetrygd, ordinær og utvidet. Delytelseskode `MS` | I bruk | teamet |
| `8001110` | Småbarnstillegg. Delytelseskode `SM` | I bruk | teamet |
| `8001010` | Finnmarkstillegg. Delytelseskode `FI` | Avviklet 1. april 2014 | teamet |
| `8001210` | Svalbardtillegg. Delytelseskode `SV` | Avviklet 1. april 2014 | teamet |

Koden kjenner igjen småbarnstillegg på `06040000` (`Utbetaling.erSmåbarnstillegg`), selv om kilden kaller nummeret gammelt. `800xxxx` er kontonumrene i Oppdragssystemet som delytelseskoden peker på (se under).

## Delytelseskode

Delytelseskoden er de to siste tegnene i stønadsklassifiseringskoden (klassekoden). Den viser hvilken konto i Oppdragssystemet (OS) ytelsen betales fra.

| Kode | Betydning | Kontonummer i Oppdrag |
|---|---|---|
| `MS` | Barnetrygd, ordinær og utvidet | `8000010` |
| `SM` | Småbarnstillegg | `8001110` |
| `FI` | Finnmarkstillegg. Avviklet 1. april 2014 | `8001010` |
| `SV` | Svalbardtillegg. Avviklet 1. april 2014 | `8001210` |

Barnetrygd betales i dag fra to kontoer i OS: `MS` og `SM`. `SM` finnes bare på klassekodene for utvidet barnetrygd.

Kilde: teamet.

## Fagområder i Oppdragssystemet

| Kode | Betydning |
|---|---|
| `IT05` | Infotrygd, barnetrygd |
| `MIT05` | Manuell korrigering av barnetrygd i Infotrygd, gjort av NØS |
| `BATR` | Barnetrygd fra ba-sak (ordinær og utvidet) |
| `BATRSMA` | Småbarnstillegg fra ba-sak |

Kilde: teamet.
