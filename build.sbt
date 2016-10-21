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
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.5.43"
)

