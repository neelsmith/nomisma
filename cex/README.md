## Data sets

- `ocre-2019-12-01.cex` is the automated conversion of OCRE RDF data downloaded on Dec. 1, 2019.
- `ocre-cite-ids.cex` is the same data set, but using ID values that are valid as object identifiers in CITE2 URNs.  The ID values differ from the last component of a nomisma.org in the following ways:
    - the identifier `9.alex.21B1.` was changed to `9.alex.21B.1`.
    - One empty record was manually removed.
    - in all identifiers, the hyphen '-' was changed to '_'

The file `ocre-url-urn-map.tsv` is a two-column tab-delimited file pairing full original URLs used as identifiers in OCRE to URNs used by the nomisma library.
