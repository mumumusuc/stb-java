cmake_minimum_required(VERSION 3.10)
project(stb)

option(FIND_JNI "find local jni library" OFF)

set(TARGET          stb)
set(PUBLIC_HDR_DIR  third_party/stb)
set(JNI_DIR         core/src/main)

include_directories(${JNI_DIR}/jni ${PUBLIC_HDR_DIR})
aux_source_directory(${JNI_DIR}/jni  JNI_SRCS)

if(FIND_JNI)
find_package(JNI REQUIRED)
message(STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
message(STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
include_directories(${JNI_INCLUDE_DIRS})
endif()

add_library(${TARGET} SHARED ${JNI_SRCS})
target_include_directories(${TARGET} INTERFACE ${PUBLIC_HDR_DIR})

file(GLOB_RECURSE PUBLIC_HDRS ${PUBLIC_HDR_DIR}/*.h)
target_sources(${TARGET} INTERFACE ${PUBLIC_HDRS})