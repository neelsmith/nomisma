# Release notes

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
