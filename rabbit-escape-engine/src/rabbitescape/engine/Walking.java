package rabbitescape.engine;

import static rabbitescape.engine.ChangeDescription.State.*;
import static rabbitescape.engine.Direction.*;
import rabbitescape.engine.ChangeDescription.State;

public class Walking implements Behaviour
{
    @Override
    public State newState( Rabbit rabbit, World world )
    {
        if ( startRise( rabbit, world ) )
        {
            return rl( rabbit, RABBIT_RISING_RIGHT_1, RABBIT_RISING_LEFT_1 );
        }
        else if ( finishRise( rabbit, world ) )
        {
            return rl( rabbit, RABBIT_RISING_RIGHT_2, RABBIT_RISING_LEFT_2 );
        }
        else if ( turn( rabbit, world ) )
        {
            return rl(
                rabbit,
                RABBIT_TURNING_RIGHT_TO_LEFT,
                RABBIT_TURNING_LEFT_TO_RIGHT
            );
        }
        else
        {
            return rl( rabbit, RABBIT_WALKING_RIGHT, RABBIT_WALKING_LEFT );
        }
    }

    private State rl( Rabbit rabbit, State rightState, State leftState )
    {
        return rabbit.dir == RIGHT ? rightState : leftState;
    }

    @Override
    public boolean behave( Rabbit rabbit, State state )
    {
        switch ( state )
        {
            case RABBIT_RISING_LEFT_1:
            case RABBIT_WALKING_LEFT:
            {
                --rabbit.x;
                return true;
            }
            case RABBIT_RISING_RIGHT_1:
            case RABBIT_WALKING_RIGHT:
            {
                ++rabbit.x;
                return true;
            }
            case RABBIT_RISING_LEFT_2:
            {
                --rabbit.y;
                --rabbit.x;
                return true;
            }
            case RABBIT_RISING_RIGHT_2:
            {
                --rabbit.y;
                ++rabbit.x;
                return true;
            }
            case RABBIT_TURNING_LEFT_TO_RIGHT:
            {
                rabbit.dir = RIGHT;
                return true;
            }
            case RABBIT_TURNING_RIGHT_TO_LEFT:
            {
                rabbit.dir = LEFT;
                return true;
            }
            default:
            {
                throw new AssertionError(
                    "Should have handled all states in Walking or before,"
                    + " but we are in state " + state.name()
                );
            }
        }
    }

    private static boolean turn( Rabbit rabbit, World world )
    {
        return world.squareBlockAt( dest( rabbit ), rabbit.y );
    }

    private static int dest( Rabbit rabbit )
    {
        return ( rabbit.dir == RIGHT ) ? rabbit.x + 1 : rabbit.x - 1;
    }

    private boolean startRise( Rabbit rabbit, World world )
    {
        return riseBlockAt( dest( rabbit ), rabbit, world );
    }

    private boolean finishRise( Rabbit rabbit, World world )
    {
        return riseBlockAt( rabbit.x, rabbit, world );
    }

    private boolean riseBlockAt( int x, Rabbit rabbit, World world )
    {
        Block block = world.getBlockAt( x, rabbit.y );
        return ( block != null && block.riseDir == rabbit.dir );
    }
}