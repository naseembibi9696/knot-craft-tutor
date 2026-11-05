package com.knotcraft.tutor.data.sample

import com.knotcraft.tutor.domain.model.Crossing
import com.knotcraft.tutor.domain.model.CrossingSide
import com.knotcraft.tutor.domain.model.KnotFamily
import com.knotcraft.tutor.domain.model.KnotSpec
import com.knotcraft.tutor.domain.model.PathTemplate

object SampleData {

    val knots: List<KnotSpec> = listOf(
        knot(
            "bowline", "Bowline", KnotFamily.LOOPS, 2,
            "A fixed loop that holds hard and still unties after a heavy load",
            PathTemplate.OVERHAND_LOOP, 0,
            "Lay a small overhand loop in the standing part and keep a long working end.",
            "Bring the working end up through that loop from underneath.",
            "Pass the end behind the standing part, keeping the big loop open.",
            "Feed the end back down through the small loop and draw both legs tight."
        ),
        knot(
            "figure_eight_loop", "Figure Eight Loop", KnotFamily.LOOPS, 1,
            "A strong loop for clipping into gear when strength beats easy untying",
            PathTemplate.FIGURE_EIGHT, 1,
            "Double the rope back on itself to make a long bight.",
            "Tie a loose figure eight using the doubled rope as one strand.",
            "Dress the knot so the two strands lie flat and parallel.",
            "Pull every strand in turn until the eight is tight and symmetrical."
        ),
        knot(
            "alpine_butterfly", "Alpine Butterfly", KnotFamily.LOOPS, 3,
            "A loop in the middle of a line that takes a pull from any direction",
            PathTemplate.DOUBLE_LOOP, 2,
            "Wrap the rope twice around your open hand to make two turns.",
            "Lift the far turn over the near one and lay it back down.",
            "Pull the remaining middle turn out under both wraps.",
            "Slide the finished loop off your hand and tighten the standing parts."
        ),
        knot(
            "bowline_bight", "Bowline On A Bight", KnotFamily.LOOPS, 3,
            "Two fixed loops in the middle of a rope with no free end needed",
            PathTemplate.DOUBLE_LOOP, 3,
            "Fold a long bight and tie a loose overhand loop with the doubled rope.",
            "Push the bight up through the loop as you would a single end.",
            "Open the bight and pass it down over the whole knot.",
            "Seat the bight behind the standing parts and tighten evenly."
        ),
        knot(
            "double_dragon", "Double Dragon Loop", KnotFamily.LOOPS, 3,
            "A secure loop for slippery line that resists shaking loose",
            PathTemplate.DOUBLE_LOOP, 4,
            "Make two parallel turns in the standing part instead of one.",
            "Thread the working end up through both turns together.",
            "Carry the end around behind the standing part.",
            "Return down through both turns and work the knot snug."
        ),
        knot(
            "anglers_loop", "Angler Loop", KnotFamily.LOOPS, 2,
            "A compact loop that stays tied in thin cord and in shock cord",
            PathTemplate.OVERHAND_LOOP, 5,
            "Form a bight and lay an overhand knot in it without tightening.",
            "Bring the bight forward across the face of the knot.",
            "Pass the bight through the upper opening of the overhand.",
            "Tighten slowly so the loop seats against the knot body."
        ),
        knot(
            "running_bowline", "Running Bowline", KnotFamily.LOOPS, 2,
            "A sliding noose for hauling logs and for retrieving dropped gear",
            PathTemplate.ROUND_TURN, 6,
            "Tie a small bowline around the standing part of the same rope.",
            "Check that the loop can slide freely along the standing part.",
            "Pass the sliding loop around the object to be hauled.",
            "Pull the standing part so the noose closes down on the load."
        ),
        knot(
            "portuguese_bowline", "Portuguese Bowline", KnotFamily.LOOPS, 3,
            "Twin adjustable loops for a bosun chair or an awkward lift",
            PathTemplate.DOUBLE_LOOP, 7,
            "Make a small loop in the standing part as for a normal bowline.",
            "Form two large loops of the size you need below it.",
            "Bring the working end up through the small loop once.",
            "Round the standing part and back down, then tighten both big loops."
        ),
        knot(
            "clove_hitch", "Clove Hitch", KnotFamily.HITCHES, 1,
            "A quick hitch to a post or a rail that adjusts while you work",
            PathTemplate.CLOVE, 0,
            "Pass the working end around the post once.",
            "Cross the end over the standing part to start the second turn.",
            "Carry the end around the post again in the same direction.",
            "Tuck the end under the crossing turn and pull both parts tight."
        ),
        knot(
            "taut_line", "Taut Line Hitch", KnotFamily.HITCHES, 2,
            "An adjustable hitch that grips under tension on tent guy lines",
            PathTemplate.CLOVE, 1,
            "Take a turn around the anchor and bring the end back alongside.",
            "Make two wraps inside the loop, working toward the anchor.",
            "Add one more wrap outside the loop on the standing part.",
            "Dress the wraps and slide the knot to set the tension."
        ),
        knot(
            "round_turn_hitches", "Round Turn And Two Half Hitches", KnotFamily.HITCHES, 1,
            "A dependable tie to a ring or a rail that takes a steady strain",
            PathTemplate.ROUND_TURN, 2,
            "Pass the end twice fully around the ring to take the load.",
            "Bring the working end across the standing part.",
            "Tie the first half hitch around the standing part.",
            "Add a second half hitch the same way and snug them together."
        ),
        knot(
            "timber_hitch", "Timber Hitch", KnotFamily.HITCHES, 1,
            "A dragging hitch for logs that frees the moment the load drops",
            PathTemplate.ROUND_TURN, 3,
            "Pass the end around the log and back around the standing part.",
            "Twist the end back around its own part three or four times.",
            "Slide the twists so they lie neatly along the rope.",
            "Pull the standing part so the hitch bites against the timber."
        ),
        knot(
            "prusik", "Prusik Hitch", KnotFamily.HITCHES, 3,
            "A friction hitch that grips when loaded and slides when it is free",
            PathTemplate.COIL_STOPPER, 4,
            "Start with a closed loop of thinner cord around the main rope.",
            "Pass the loop through itself to make a girth hitch.",
            "Wrap the loop through two more times inside the first wrap.",
            "Dress the wraps side by side and test that the hitch grips."
        ),
        knot(
            "cleat_hitch", "Cleat Hitch", KnotFamily.HITCHES, 1,
            "The standard way to secure a boat line to a deck cleat",
            PathTemplate.CLEAT, 5,
            "Take a full turn around the base of the cleat first.",
            "Cross over the top and under the far horn in a figure eight.",
            "Repeat the figure eight once so both horns are loaded.",
            "Finish with a locking turn and leave the tail lying flat."
        ),
        knot(
            "rolling_hitch", "Rolling Hitch", KnotFamily.HITCHES, 2,
            "A hitch that takes a lengthwise pull along a pole or a loaded rope",
            PathTemplate.CLOVE, 6,
            "Wrap the end around the pole toward the direction of pull.",
            "Add a second wrap crossing over the first one.",
            "Carry the end past the crossing to the far side.",
            "Finish with a half hitch and ease the knot until it grips."
        ),
        knot(
            "highwayman_hitch", "Highwayman Hitch", KnotFamily.HITCHES, 2,
            "A quick release hitch that holds a load but frees with one pull",
            PathTemplate.CLEAT, 7,
            "Pass a bight of rope behind the rail from the far side.",
            "Push a bight of the standing part through that first bight.",
            "Pull the first bight down tight on the second one.",
            "Push a bight of the working end through and set the knot."
        ),
        knot(
            "sheet_bend", "Sheet Bend", KnotFamily.BENDS, 1,
            "The everyday join for two ropes of different thickness",
            PathTemplate.SHEET_BEND, 0,
            "Make a bight in the thicker rope and hold it open.",
            "Pass the thinner end up through the bight from below.",
            "Carry the thin end around behind both legs of the bight.",
            "Tuck the end under its own part and pull all four legs."
        ),
        knot(
            "double_sheet_bend", "Double Sheet Bend", KnotFamily.BENDS, 2,
            "A sheet bend with a second turn for ropes of very unequal size",
            PathTemplate.SHEET_BEND, 1,
            "Tie an ordinary sheet bend and leave it loose.",
            "Take the thin end around behind the bight a second time.",
            "Tuck it under its own part beside the first tuck.",
            "Work both turns snug so they lie side by side."
        ),
        knot(
            "carrick_bend", "Carrick Bend", KnotFamily.BENDS, 3,
            "A flat bend for heavy line that stays easy to undo after loading",
            PathTemplate.FIGURE_EIGHT, 2,
            "Form a loop in the first rope with the end over the standing part.",
            "Lay the second rope across the loop, over and then under.",
            "Weave the second end over, under and over the first rope.",
            "Pull the standing parts so the flat pattern capsizes and sets."
        ),
        knot(
            "zeppelin_bend", "Zeppelin Bend", KnotFamily.BENDS, 3,
            "A jam proof bend that comes apart after any load you can apply",
            PathTemplate.DOUBLE_LOOP, 3,
            "Make a loop in each rope, one clockwise and one the other way.",
            "Stack the two loops face to face so they interlock.",
            "Pass each working end through the centre of both loops.",
            "Send the ends in opposite directions and tighten slowly."
        ),
        knot(
            "double_fisherman", "Double Fisherman Bend", KnotFamily.BENDS, 2,
            "A permanent join for cord loops and for climbing slings",
            PathTemplate.COIL_STOPPER, 4,
            "Lay the two rope ends alongside each other pointing opposite ways.",
            "Wrap one end twice around both ropes and tuck it back through.",
            "Repeat the double wrap with the other end from its side.",
            "Pull the standing parts so the two barrels slide together."
        ),
        knot(
            "water_knot", "Water Knot", KnotFamily.BENDS, 1,
            "The join for flat webbing where a round knot would roll",
            PathTemplate.OVERHAND_LOOP, 5,
            "Tie a loose overhand knot in one end of the webbing.",
            "Feed the other end into the knot from the opposite side.",
            "Follow the first knot exactly, keeping the webbing flat.",
            "Tighten and leave a generous tail on each side."
        ),
        knot(
            "hunters_bend", "Hunter Bend", KnotFamily.BENDS, 2,
            "A neat symmetrical bend for synthetic rope that resists slipping",
            PathTemplate.FIGURE_EIGHT, 6,
            "Hold the two ropes side by side with the ends pointing opposite ways.",
            "Make a single loop in both ropes together.",
            "Pass each end through the centre of the loop from its own side.",
            "Draw the standing parts apart until the bend locks."
        ),
        knot(
            "figure_eight_stopper", "Figure Eight Knot", KnotFamily.STOPPERS, 1,
            "The standard stopper that keeps a line from running out of a block",
            PathTemplate.FIGURE_EIGHT, 0,
            "Make a loop with the working end crossing over the standing part.",
            "Carry the end around behind the standing part.",
            "Bring the end down through the original loop.",
            "Pull the standing part so the eight shape stays visible."
        ),
        knot(
            "overhand_knot", "Overhand Knot", KnotFamily.STOPPERS, 1,
            "The simplest stopper and the start of many other knots",
            PathTemplate.OVERHAND_LOOP, 1,
            "Form a small loop near the end of the rope.",
            "Pass the working end through the loop once.",
            "Hold both parts and slide the knot where you want it.",
            "Tighten firmly and trim the tail to a hand width."
        ),
        knot(
            "double_overhand", "Double Overhand", KnotFamily.STOPPERS, 2,
            "A bulkier stopper for cord that slips through a plain overhand",
            PathTemplate.COIL_STOPPER, 2,
            "Start exactly as for a plain overhand knot.",
            "Pass the working end through the loop a second time.",
            "Roll the two turns so they sit beside each other.",
            "Pull steadily until the barrel forms and locks."
        ),
        knot(
            "ashley_stopper", "Ashley Stopper", KnotFamily.STOPPERS, 2,
            "A wide bulky stopper that blocks a large opening without jamming",
            PathTemplate.CLOVE, 3,
            "Tie a loose slip knot in the working end.",
            "Pass the end through the loop of the slip knot.",
            "Pull the standing part so the slip knot closes on the end.",
            "Dress the three leaf shape and tighten evenly."
        ),
        knot(
            "stevedore", "Stevedore Knot", KnotFamily.STOPPERS, 2,
            "A long stopper with extra turns for rope running fast through gear",
            PathTemplate.COIL_STOPPER, 5,
            "Begin a figure eight but add an extra turn around the standing part.",
            "Keep the turns stacked neatly along the rope.",
            "Pass the working end back through the first loop.",
            "Tighten so the knot forms a long even barrel."
        ),
        knot(
            "heaving_line", "Heaving Line Knot", KnotFamily.STOPPERS, 3,
            "A weighted end knot that carries a thrown line across a gap",
            PathTemplate.CLEAT, 6,
            "Make a long bight at the end of the rope.",
            "Wrap the working end around the bight six or seven times.",
            "Keep each wrap tight and touching the one before it.",
            "Tuck the end through the remaining bight and pull it closed."
        ),
        knot(
            "monkey_fist", "Monkey Fist", KnotFamily.STOPPERS, 3,
            "A woven ball at the end of a line for weight and for a sure grip",
            PathTemplate.COIL_STOPPER, 7,
            "Make three turns around two fingers to start the first axis.",
            "Wrap three more turns across them at a right angle.",
            "Pass three final turns through the first set to close the ball.",
            "Work each turn tight in order until the fist is round and firm."
        )
    )

    private fun knot(
        id: String,
        name: String,
        family: KnotFamily,
        difficulty: Int,
        use: String,
        template: PathTemplate,
        variation: Int,
        vararg steps: String
    ): KnotSpec = KnotSpec(
        id = id,
        name = name,
        family = family,
        difficulty = difficulty,
        use = use,
        steps = steps.toList(),
        template = template,
        variation = variation,
        crossings = crossingsFor(template)
    )

    private fun crossingsFor(template: PathTemplate): List<Crossing> = when (template) {
        PathTemplate.OVERHAND_LOOP -> listOf(
            Crossing(0.33f, CrossingSide.OVER),
            Crossing(0.68f, CrossingSide.UNDER)
        )
        PathTemplate.DOUBLE_LOOP -> listOf(
            Crossing(0.26f, CrossingSide.OVER),
            Crossing(0.52f, CrossingSide.UNDER),
            Crossing(0.78f, CrossingSide.OVER)
        )
        PathTemplate.ROUND_TURN -> listOf(
            Crossing(0.30f, CrossingSide.UNDER),
            Crossing(0.62f, CrossingSide.OVER)
        )
        PathTemplate.FIGURE_EIGHT -> listOf(
            Crossing(0.25f, CrossingSide.OVER),
            Crossing(0.50f, CrossingSide.UNDER),
            Crossing(0.75f, CrossingSide.OVER)
        )
        PathTemplate.CLOVE -> listOf(
            Crossing(0.28f, CrossingSide.OVER),
            Crossing(0.58f, CrossingSide.UNDER),
            Crossing(0.84f, CrossingSide.OVER)
        )
        PathTemplate.SHEET_BEND -> listOf(
            Crossing(0.34f, CrossingSide.UNDER),
            Crossing(0.66f, CrossingSide.OVER)
        )
        PathTemplate.CLEAT -> listOf(
            Crossing(0.22f, CrossingSide.OVER),
            Crossing(0.45f, CrossingSide.UNDER),
            Crossing(0.70f, CrossingSide.OVER),
            Crossing(0.88f, CrossingSide.UNDER)
        )
        PathTemplate.COIL_STOPPER -> listOf(
            Crossing(0.38f, CrossingSide.OVER),
            Crossing(0.72f, CrossingSide.UNDER)
        )
    }
}
