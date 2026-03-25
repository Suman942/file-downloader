//package com.suman.network_library
//
//import com.suman.network_library.utils.FileNameUtils
//import org.junit.Test
//import org.junit.Assert.*
//class FileNameUtilTest {
//    @Test
//    fun filename_has_extension_true(){
//        val result = FileNameUtils.hasExtension(
//            "image.jpg"
//        )
//        assertTrue(result)
//    }
//
//    @Test
//    fun file_name_has_not_extension(){
//        val result = FileNameUtils.hasExtension("image")
//        assertFalse(result)
//    }
//
//    @Test
//    fun file_name_has_random_extension(){
//        val result = FileNameUtils.hasExtension("image.abc")
//        assertFalse(result)
//    }
//}