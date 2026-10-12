# familie-ba-infotrygd-replika

REST-tjeneste for oppslag mot historiske barnetrygddata i Infotrygd via replikert Oracle-database.

## Nyttig dokumentasjon relatert til Infotrygd
https://confluence.adeo.no/spaces/MODNAV/pages/358694924/Databaser
https://confluence.adeo.no/spaces/TFA/pages/346923538/Sakskoder+sakstyper+i+Infotrygd
https://navno.sharepoint.com/sites/fag-og-ytelser-fagsystemer/Delte%20dokumenter/Forms/AllItems.aspx?id=/sites/fag-og-ytelser-fagsystemer/Delte%20dokumenter/Infotrygd/Brukerh%C3%A5ndb%C3%B8ker%20Infotrygd/14%20-%20Brukerh%C3%A5ndbok%20Infotrygd%20Saksbehandlingsrutinen%20-%20ny%20versjon%20061216%20%281%29.pdf&parent=/sites/fag-og-ytelser-fagsystemer/Delte%20dokumenter/Infotrygd/Brukerh%C3%A5ndb%C3%B8ker%20Infotrygd
https://confluence.adeo.no/spaces/TFA/pages/409984169/Diverse+koder+i+Infotrygd

## Hva tjenesten tilbyr

- Base path: `/infotrygd/barnetrygd`
- Swagger UI (lokalt): `http://localhost:8080/swagger-ui/index.html`
- OpenAPI (lokalt): `http://localhost:8080/v3/api-docs`

### Endepunkter

| Metode | Path | Beskrivelse |
|---|---|---|
| `POST` | `/infotrygd/barnetrygd/lopende-barnetrygd` | Sjekker om det finnes lopende barnetrygd for bruker/barn |
| `POST` | `/infotrygd/barnetrygd/aapen-sak` | Sjekker om det finnes apen sak til beslutning |
| `POST` | `/infotrygd/barnetrygd/stonad` | Henter stonad for bruker/barn |
| `POST` | `/infotrygd/barnetrygd/saker` | Henter saker for bruker/barn |
| `GET` | `/infotrygd/barnetrygd/stonad/{id}` | Henter stonad pa id (deprecated) |
| `POST` | `/infotrygd/barnetrygd/stonad/sok` | Henter stonad pa personkey/perioder/region |
| `POST` | `/infotrygd/barnetrygd/brev` | Sjekker om brev er sendt forrige maned |
| `POST` | `/infotrygd/barnetrygd/pensjon` | Henter barnetrygdperioder til pensjon |
| `GET` | `/infotrygd/barnetrygd/pensjon?aar=<ar>` | Finner personer med barnetrygd i gitt ar |
| `POST` | `/infotrygd/barnetrygd/utvidet` | Henter utvidet barnetrygd/smabarnstillegg for Bisys |

## Tilgang og sikkerhet

- Endepunkter er beskyttet med Azure AD JWT (`issuer=azuread`).
- Kall krever enten rollen `access_as_application` eller medlemskap i en av teamfamilie-gruppene konfigurert i `application.yml`.

## Lokalt oppsett

### Forutsetninger

- Java 25+
- Maven 3.8+
- Docker (for tester som bruker Testcontainers)
- GitHub-tilgang til private Maven-pakker

### Lokal kjøring

Start TestMain klassen i IDE for å kjøre tjenesten lokalt. Dette starter en embedded Tomcat på `http://localhost:8080`.

## Bygg, test og kjør

```bash
mvn clean install
```

Bygget jar ligger i `target/familie-ba-infotrygd-0.0.1-SNAPSHOT.jar`.

## Observability

- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`
- Prometheus: `http://localhost:8080/actuator/prometheus`

## Databaseoversikt

Brukte tabeller per tjeneste/endepunkt er dokumentert i endepunkt /tables

## Eierskap

- Team: `@navikt/team-baks` (se `CODEOWNERS`)

## Referanser

- Confluence: <https://confluence.adeo.no/display/MODNAV/Databaser>

## Kode generert av GitHub Copilot
Dette repoet bruker GitHub Copilot til å generere kode.

