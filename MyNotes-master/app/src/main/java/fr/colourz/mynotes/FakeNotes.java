package fr.colourz.mynotes;

import java.util.ArrayList;
import java.util.List;



public class FakeNotes {
    public Note getWelcomeNote(){
        Note note = new Note(
                "Welcome!",
                "Welcome to MyNotes.<br/><br/>With MyNotes, you can use different text formats, such as <b>bold</b>, <i>italic</i> and <u>underline</u>. But you can also set different <font color='#EE6352'>co</font><font color='#3454d1'>lo</font><font color='#34d1bf'>rs</font>!<br/><br/>Also, you can use bullet lists:<br/> \u2022  this<br/> \u2022  is a<br/> \u2022  bullet list.<br/><br/>Or you can<br/>\u0009\u0009\u0009\u0009indent<br/>\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009your<br/>\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0009text<br/><br/>You can even use hypertext links. Try to click <a href='https://developer.android.com/index.html'>here</a> for example!",
                "1515586092",
                "1516018092"
        );
        return note;
    }
    public List<Note> getFakeNotes(){
        List<Note> list = new ArrayList<>();
        list.add(new Note(
                "Lorem Ipsum",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut et lacus pulvinar, tristique lectus vitae, laoreet magna. Pellentesque quis turpis mollis, dapibus arcu in, bibendum augue. Nullam sed justo at urna convallis molestie. Phasellus aliquet at mauris id semper. Sed nec velit imperdiet lectus ultrices congue et quis mi. Cras velit orci, vulputate eu scelerisque eget, finibus non lectus. Quisque ultricies dui vitae nisl consectetur, non fermentum augue condimentum. Proin quis nisl in lorem luctus dignissim. Cras ultricies volutpat ipsum, molestie suscipit tellus tempor non. Suspendisse molestie odio a metus pharetra ultrices.",
                "1515586092",
                "1516018092"

        ));
        list.add(new Note(
                "Test",
                "Pellentesque condimentum ligula eu massa volutpat accumsan. Aliquam vitae placerat orci. Nam est quam, venenatis in sem sed, dapibus fringilla orci. Suspendisse potenti. Vivamus sed dignissim lorem, sit amet feugiat ante. Nulla sagittis ultrices sapien id auctor. Nulla pharetra enim eget erat pretium tristique. Morbi rhoncus, ex vel molestie euismod, ante eros iaculis massa, vel accumsan quam lorem ac lorem. Maecenas a urna id neque laoreet facilisis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse luctus sed nisl id molestie. Sed aliquam sem enim, sit amet bibendum diam dictum eu. Nunc porttitor eleifend elit quis rhoncus.",
                "1509192492",
                "1510488492"

        ));
        list.add(new Note(
                "Hello world!",
                "Pellentesque blandit fringilla felis, vitae bibendum massa consectetur pretium. Mauris tincidunt est at elit rutrum, at dictum felis elementum. Duis nibh lacus, tempus in efficitur posuere, posuere quis massa. Etiam magna purus, commodo ut tincidunt nec, venenatis sed nisi. Maecenas enim elit, imperdiet eu mi et, ullamcorper cursus lacus. Proin eget risus eget purus pulvinar pretium sed et ipsum. Pellentesque rhoncus enim a tincidunt tincidunt. Sed auctor odio sed purus vestibulum, in suscipit ante hendrerit. Mauris at justo lobortis quam tincidunt scelerisque. Nunc dapibus massa sodales elit laoreet pulvinar. Aenean sit amet dolor in dui congue bibendum. Maecenas vulputate, erat eget tempor euismod, ante quam efficitur urna, nec pharetra felis purus non nisi. In vulputate pellentesque metus a tristique. Nullam at purus ipsum. Vivamus elementum urna ac dui feugiat tristique.",
                "1521979692",
                "1521979692"

        ));
        list.add(new Note(
                "Shopping list",
                "Aliquam at nulla a magna bibendum cursus eget ac urna. Etiam at imperdiet est. Vivamus ac facilisis lectus. Cras et congue sem, sit amet gravida arcu. Curabitur sit amet ipsum est. Sed sed risus sit amet odio aliquam aliquet sed ac nisi. Pellentesque sed justo sed arcu dignissim semper quis id lacus. Fusce dignissim augue justo, vitae hendrerit ligula venenatis mattis. Nam nunc nisl, sagittis non elit eu, tincidunt convallis mi. Nulla tempus justo ultricies, posuere nulla suscipit, blandit ante. Curabitur in turpis ut orci tincidunt gravida ac a quam. Sed vel suscipit mi.",
                "1521202092",
                "1521202092"

        ));
        list.add(new Note(
                "Trip to Cork",
                "Integer a sem eu orci aliquet aliquam. Integer maximus diam in justo pharetra semper. Nulla eu orci ultricies, hendrerit lectus ac, suscipit arcu. In sit amet leo orci. Cras tristique euismod pretium. Curabitur tristique lectus malesuada, finibus purus eget, mattis sem. Nulla facilisi. Praesent vehicula orci eros, et ultrices lectus condimentum vitae. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec eleifend tincidunt turpis non posuere. Maecenas pharetra massa neque, ac tristique leo venenatis at. Donec vulputate, tellus et posuere pretium, massa nunc pellentesque augue, in posuere risus leo vel lorem. Sed nec lorem leo. Suspendisse vulputate ante a interdum rutrum. Maecenas tempus lacinia purus, vel laoreet lorem ultricies sed. Aenean fringilla, metus ultrices imperdiet luctus, elit nunc luctus metus, quis consectetur diam urna ut sapien.",
                "1518178092",
                "1519560492"

        ));
        list.add(new Note(
                "Griffith Notes",
                "Aenean iaculis aliquet nulla id venenatis. Nunc a tincidunt neque, in gravida dolor. Aenean congue luctus nisi et aliquet. Fusce augue metus, cursus nec lectus id, luctus consectetur erat. Pellentesque sit amet dignissim nisl. Nullam commodo, augue ut rhoncus egestas, purus sem ultricies purus, a luctus diam mi id nunc. Nunc lobortis ex non hendrerit facilisis.",
                "1503576492",
                "1505477292"

        ));
        return list;
    }
}
