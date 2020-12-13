# Build for markturn

cmake_minimum_required(VERSION 3.18.4)

# project info # @NOTE Comment doesn't add anything
project(markturn VERSION "0.9.5")
message("VERSION: " ${PROJECT_VERSION})
message("MAJOR VERSION: " ${PROJECT_VERSION_MAJOR})

option(LINK_STATIC "Build with static library" OFF)
message("LINK_STATIC: ${LINK_STATIC}")

find_package(CURL REQUIRED)
message("-- curl include: " ${CURL_INCLUDE_DIR})
message("-- curl library: " ${CURL_LIBRARY})

find_package(LibXml2 REQUIRED)
message("-- libxml2 include: " ${LIBXML2_INCLUDE_DIR})
message("-- libxml2 library: " ${LIBXML2_LIBRARY})

# Use find path to find json and yaml
find_path(jsonLib REQUIRED NAMES json/json.h PATHS /usr/include/jsoncpp /usr/include/json)
find_path(yamlLib REQUIRED NAMES yaml.h PATHS /usr/include/yaml-cpp)

# Builds markturn
set(markturn src/markturn.cpp)
add_executable(markturn ${markturn})

# Builds with static or library
if(LINK_STATIC)
    message("-- Static library")
    target_link_libraries(markturn PUBLIC markturn_static)
else()
    message("-- Shared library")
    target_link_libraries(markturn PUBLIC markturn_shared)
endif()

install(TARGETS markturn RUNTIME)

# Object library 
file(GLOB LIBRARY_SOURCES "src/libmarkturn.cpp" "src/*Format.cpp")
set(LIB_DIRS ${yamlLib} ${jsonLib} ${LIBXML2_INCLUDE_DIR})
add_library(markturnobj OBJECT ${LIBRARY_SOURCES})
target_include_directories(markturnobj PUBLIC ${LIB_DIRS})

# Static libmarkturn library
add_library(markturn_static STATIC $<TARGET_OBJECTS:markturnobj>)
target_link_libraries(markturn_static PUBLIC ${CURL_LIBRARIES})
install(TARGETS markturn_static LIBRARY)

# Shared libmarkturn library
add_library(markturn_shared SHARED $<TARGET_OBJECTS:markturnobj>)
target_link_libraries(markturn_shared PUBLIC ${CURL_LIBRARIES})
set_target_properties(markturn_shared PROPERTIES VERSION ${PROJECT_VERSION})
set_target_properties(markturn_shared PROPERTIES SOVERSION ${PROJECT_VERSION_MAJOR})
install(TARGETS markturn_shared LIBRARY)

# Setup testing
enable_testing()

# all .cpp files in test/ are independent test programs
file(GLOB TEST_SOURCES test/*.cpp)
foreach(TESTFILE ${TEST_SOURCES})
    get_filename_component(TEST_NAME ${TESTFILE} NAME_WLE)
    add_executable(${TEST_NAME} ${TESTFILE})

    if(LINK_STATIC)
        target_link_libraries(${TEST_NAME} PUBLIC markturn_static)
        target_include_directories(${TEST_NAME} PUBLIC ${CMAKE_SOURCE_DIR}/src)
        add_test(NAME ${TEST_NAME} COMMAND ${TEST_NAME})
    else()
        target_link_libraries(${TEST_NAME} PUBLIC markturn_shared)
        target_include_directories(${TEST_NAME} PUBLIC ${CMAKE_SOURCE_DIR}/src)
        add_test(NAME ${TEST_NAME} COMMAND ${TEST_NAME})
    endif()

#  message("-- Added test" ${TEST_NAME} command ${TEST_NAME} progress ${TESTFILE})
endforeach()


# Generate Simple Package
# @NOTE Keep comments consistent, even with capitalization, i.e., "Generate simple package"
set(CPACK_PACKAGE_NAME "markturn")
set(CPACK_PACKAGE_VERSION_MAJOR "1")
set(CPACK_PACKAGE_VERSION_MINOR "0")
set(CPACK_PACKAGE_VERSION_PATCH "0")
set(CPACK_PACKAGE_CONTACT "yp16@zips.uakron.edu")
include(CPack)
