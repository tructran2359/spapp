package com.spgroup.spapp

import org.junit.Assert.assertEquals
import org.junit.Test
import org.unbescape.html.HtmlEscape

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    // test before format: < &quot;  &num;  &dollar;  &percnt;  &amp;  &apos;  &lpar;  &rpar;  &ast;  &plus;  &comma;  &minus;  &period;  &sol;  &colon;  &semi;  &lt;  &equals;  &gt;  &quest;  &commat;  &lsqb;  &bsol;  &rsqb;  &Hat;  &lowbar;  &grave;  &lcub;  &verbar;  &rcub; >
    // test with html char: < "  #  $  %  &  '  (  )  *  +  ,  −  .  /  :  ;  <  =  >  ?  @  [  \  ]  ^  _  `  {  |  } >
    @Test
    fun `test special character`() {
        val dummy = "This is a \r\n\t test with html char: < &quot;  &num;  &dollar;  &percnt;  &amp;  &apos;  &lpar;  &rpar;  &ast;  &plus;  &comma;  &minus;  &period;  &sol;  &colon;  &semi;  &lt;  &equals;  &gt;  &quest;  &commat;  &lsqb;  &bsol;  &rsqb;  &Hat;  &lowbar;  &grave;  &lcub;  &verbar;  &rcub; >"
        val noHtml = HtmlEscape.unescapeHtml(dummy)

        assertEquals("This is a \r\n\t test with html char: < \"  #  \$  %  &  '  (  )  *  +  ,  −  .  /  :  ;  <  =  >  ?  @  [  \\  ]  ^  _  `  {  |  } >", noHtml)
    }
}
