cmake_minimum_required(VERSION 3.10)
project(stb)

option(FIND_JNI "find local jni library" OFF)

set(TARGET          stb)
set(PUBLIC_HDR_DIR  third_party/stb)
set(JNI_DIR         core/src/main/jni)
set(JNI_SRCS        ${JNI_DIR}/com_mumumusuc_stb_StbImageNative.cpp)

include_directories(${JNI_DIR} ${PUBLIC_HDR_DIR})

if(FIND_JNI)
find_package(JNI REQUIRED)
message(STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
message(STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
include_directories(${JNI_INCLUDE_DIRS})
endif()

add_library(${TARGET} SHARED ${JNI_SRCS} dummy.cpp)
target_include_directories(${TARGET} INTERFACE ${PUBLIC_HDR_DIR})

file(GLOB_RECURSE PUBLIC_HDRS ${PUBLIC_HDR_DIR}/*.h)
target_sources(${TARGET} INTERFACE ${PUBLIC_HDRS})