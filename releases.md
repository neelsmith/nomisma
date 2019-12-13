# Release notes


**2.2.0**: adds implementation of MID orthography trait for OCRE diplomatic and normalized texts.

**2.1.0**: adds `++` function for adding concrete `IssueCollections`, and better String formatting of `YearRange` objects.

**2.0.1**:  eliminates duplicate records from nomisma.org RDF before attempting to interpret as object model.

**2.0.0**:  generalizes `IssueCollection` trait and implements for `Ocre`, `Crro` and `GenericIssuesCatalog`.

**1.8.0**:  adds CRRO classes.

**1.7.0**: adds the `UrlManager` class, and an `issue` function on `Ocre`objects.

**1.6.0**: `Ocre` generates `Histogram` objects for property names.

**1.5.0**: new functions on `Ocre` objects for finding lists of values for particular properties, and for creating a new `Ocre` limited to issues containing values for a given property.

**1.4.3**: catch exceptional case in RDF parsing where nomisma.org does NOT use an attribute with a URL as identifier for a portrait but instead has raw text content of the XML node.  Sigh.

**1.4.2**: bug fixes in RDF parsing of date and portrait identifiers.

**1.4.1**: use proper function for converting RIC URL to ID value in Potrait and YearRange objects.

**1.4.0**: add function to build an `Ocre` directly from a URL to data in CEX format.

**1.3.0**: add functions to directly build an `Ocre` from RDF data in a file or available from a URL.

**1.2.0**: add logging support with airframe logger.

**1.1.0**: adds functions to create `cex` of `Ocre` and to generate an `ohco2` text corpus for all legends in `Ocre`.

**1.0.0**: `RdfOcre` class instantiates a full `RdfOcre` instance from RDF source file currently downloaded from `nomisma.org`.

**0.7.0**: create new `Ocre` class with numerous useful corpus functions.

**0.6.0**: clean separation of JVM and JS


**0.5.0**: reorganize `OcreIssue` structure.


**0.4.0**: support CEX data sources for classes related to OCRE.

**0.3.0**:  update libraries.

**0.2.0**:  test release not suitable for production work.
