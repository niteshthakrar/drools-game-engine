/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.workshop.core.tests;

import java.util.ArrayList;
import java.util.List;
import org.drools.workshop.core.CommandExecutor;
import org.drools.workshop.core.Context;
import org.drools.workshop.core.tests.cmds.ExploreCommand;
import org.drools.workshop.core.tests.cmds.OpenDoorCommand;
import org.drools.workshop.core.tests.cmds.PickItemCommand;
import org.drools.workshop.model.Player;
import org.drools.workshop.model.house.Door;
import org.drools.workshop.model.house.House;
import org.drools.workshop.model.house.Room;
import org.drools.workshop.model.items.Chest;
import org.drools.workshop.model.items.Item;
import org.drools.workshop.model.items.Key;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author salaboy
 */
public class PlayersCommandsTest {

    public PlayersCommandsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void hello() {
        Player player = new Player( "salaboy" );

        House house = new House( "my mansion" );

        /* Room A:  
        *  - 1 Door
        *  - 1 chest with a key 
         */
        Room roomA = new Room( "Room A" );
        List<Door> doorsA = new ArrayList<Door>();
        Door doorA = new Door( "Door A" );
        doorA.setLeadsTo( "Room B" );
        doorsA.add( doorA );
        roomA.setDoors( doorsA );

        List<Item> itemsRoomA = new ArrayList<Item>();
        Chest chestA = new Chest( "Chest A" );
        List<Item> itemsChestA = new ArrayList<Item>();
        Key keyA = new Key( "Door A" );
        itemsChestA.add( keyA );
        chestA.setItems( itemsChestA );
        itemsRoomA.add( chestA );

        roomA.setItems( itemsRoomA );

        List<Room> rooms = new ArrayList<Room>();
        rooms.add( roomA );
        house.setRooms( rooms );

        List<String> initPlayer = new ArrayList<String>();
        initPlayer.add( player.getName() );
        roomA.setPeopleInTheRoom( initPlayer );

        Assert.assertEquals( 1, house.getRooms().size() );
        Assert.assertTrue( roomA.getPeopleInTheRoom().contains( player.getName() ) );
        Assert.assertTrue( player.getItems().isEmpty() );

        CommandExecutor executor = new CommandExecutor();
        List<String> messages = new ArrayList<String>();
        Context context = new Context();
        context.getData().put( "player", player );
        context.getData().put( "room", roomA );
        context.getData().put( "messages", messages );

        List<Item> items = executor.execute( new ExploreCommand(), context );
        assertEquals( 1, items.size() );
        Item item = items.get( 0 );
        assertTrue( item instanceof Chest );
        Chest chest = ( Chest ) item;
        assertEquals( 1, chest.getItems().size() );

        item = chest.getItems().get( 0 );
        assertTrue( item instanceof Key );
        Key key = ( Key ) item;

        executor.execute( new PickItemCommand( key ), context );

        assertTrue( messages.contains( "Item picked!" ) );

        executor.execute( new OpenDoorCommand(), context );

        assertTrue( messages.contains( "Door Opened!" ) );

    }
}
