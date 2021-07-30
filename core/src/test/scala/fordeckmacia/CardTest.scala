package fordeckmacia

import munit.FunSuite

class CardTest extends FunSuite:
  test("invalid cards") {
    intercept[IllegalArgumentException](Card(100, Faction.Bilgewater, 1))
    intercept[IllegalArgumentException](Card(1, Faction.Bilgewater, 1000))
  }

  test("invalid card codes") {
    assertEquals(Card.fromCode(""), None)
    assertEquals(Card.fromCode("1"), None)
    assertEquals(Card.fromCode("11N"), None)
    assertEquals(Card.fromCode("ABCDEFG"), None)
    assertEquals(Card.fromCode("11ZZ111"), None)
    assertEquals(Card.fromCode("11NXABC"), None)
  }
