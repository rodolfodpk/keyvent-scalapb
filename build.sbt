scalaVersion := "2.11.8"

PB.targets in Compile := Seq(
  scalapb.gen(singleLineToString = true) -> (sourceManaged in Compile).value

)

// scalapb and json
libraryDependencies ++= Seq(
  // For finding google/protobuf/descriptor.proto
  "com.trueaccord.scalapb" %% "scalapb-json4s" % "0.1.2",
  "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.5.43" % "protobuf",
  // grpc
  "io.grpc" % "grpc-netty" % "1.0.1",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.5.43",
  // apache camel
  "org.apache.camel" % "camel-core" % "2.18.0",
  // validation
 // "com.wix" %% "accord-core" % "0.6",
  "io.underscore" %% "validation" % "0.0.2",
  // database
  "com.typesafe.slick" % "slick_2.11" % "3.1.1",
  "com.h2database" % "h2" % "1.3.174"
)

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.8")

