<h1>IPADDREADER</h1>

This program count unique ip-address from file. File can be large like <code><a href="https://ecwid-vgv-storage.s3.eu-central-1.amazonaws.com/ip_addresses.zip">this one</a></code>.

<details close>
    <summary>Two cents about a task</summary>
    <div>
        <p>
            Here is a huge (100gb and more) file, that contains ipv4 address <i>(each one on new line)</i> and we need to
            count all <b>unique</b> ip-address as fast if we can and use low memory as possible.
        </p>
        <p>
            For example we have next ip address in file:
            <div style="background: #20200b">
                1.1.1.1
                </br>2.2.2.2
                </br>3.3.3.3
                </br>1.1.1.1
            </div>
        </p>
        <p>
            And here we can say <b>unique ip address is that:</b> 
            <ul>
                <li>
                    Just met in parsing process
                    <div>
                        <div style="background: #20200b">
                            <span style="color: green">
                                <b><i>1.1.1.1
                                </br>2.2.2.2
                                </br>3.3.3.3</i></b></span>
                                </br><span style="color: pink">1.1.1.1</span>
                        </div>
                        Answer is <b>3</b> (1.1.1.1, 2.2.2.2, 3.3.3.3).
                    </div>
                </li>
                <li>
                    Appear in file only once
                    <div>
                        <div style="background: #20200b">
                            <span style="color: pink">1.1.1.1</span>
                            </br><span style="color: green"><b><i>2.2.2.2
                            </br>3.3.3.3</i></b></span>
                            </br><span style="color: pink">1.1.1.1</span>
                        </div>
                        Answer is <b>2</b> (2.2.2.2, 3.3.3.3; 1.1.1.1 contained twice, so it's not unique).
                    </div>
                </li>
            </ul>
        </p>
        <p>
            What variant is exactly correct? Hmm... I decide to implement both: 
            <a>all unique</a> and <a>contains in file once</a>
        </p>
    </div>
</details>
<hr>
<h2>How it works?</h2>

<details close>
    <summary>Theory</summary>
    <div>
        <p>
            Ok. How we can count unique ip-address?
            First idea what comes is <code>awk sort | uniq</code>, <b>oh yeah, we did it</b>!!! But we take a different approach. We're not using any fancy stuff like databases, 
            cloud services, or modern features. Instead, we're looking back to the old days and thinking about basic things like C, Assembler, bits, registers, memory indexing, 
            and other similar stuff.... machine code =)...
        </p>
        <p>
            Alright, let's imagine that we can read all IP addresses from the file directly into memory (without dumbs) and then check for uniqueness using just one loop. Each IP address 
            typically takes between <b>7</b> to <b>15</b> bytes, ranging from addresses like <b>0.0.0.0</b> to <b>255.255.255.255</b>. So, the total size for all possible IPv4 addresses 
            is 7 * 4294967296 (all possible IPv4) = 30064771072 bytes (and it's just a minimum! we calculate as all ips takes 7 bytes), which is roughly 28.67 GB. 
            Well, that's not too much for today's standards, but it's still quite huge.
        </p>
        <p>
            Hmm... How about "compress" them to 512mb?
            </br>
            <picture>
                <img src="https://iili.io/J6fOCwF.jpg" alt="J6fOCwF.jpg" border="0" />
            </picture>
        </p>
        <p>
            We don't need store ip address as is, only information about his apperiance.
            I use Java and haven't direct access to memory <i>(yes, I know about HotSpot and <b>sun.misc.Unsafe.getAddress()</b>
            and <b>.putAddress()</b>, but let's use regular Java17 and only JavaCore...)</i>, so we need works with memory throuth wrappers like
            arrays or variables. The first problem that we have is max count of indexing array's element and variant we can't manipulate
            of large count of index, the second is elements (that need memory too).
        </p>
    </div>
</details>

<details close>
    <summary>Practice</summary>
    <p>
        <p>
            If we just keep only fact of appear any ip - we need only one bit [0 - not appear, 1 - appear], and need only 4294967296
            bits - only 512mb. Looks interesting... but how keep it?
        </p>
        <p>
            There 
        </p>
        <p>
            The biggest primitive type in Java is 8-byte <code>long</code>, so we can keep information about 64 different
            ip-address in one long and now need <code>4294967296 / 64 = 67108864</code> elements.
            Ok, init 2 arrays (cuz Java hasn't unsign type)
            <p>
                <div style="background: #20200b">
                    long[] bankH = new long[33554432];
                    </br>long[] bankL = new long[33554432]; // for 'minus' values
                </div>
            </p>
        </p>
        <p>
            And here start a magic:
            <ul>
                <li>we count index of long[] element from first 28bit of our int-ip - call it bank</li>
                <li>next we need index for position in elements bit - we use last 4bits of our int-ip, call it cell</li>
                <li>if stored bit is 0 - we have a new unique ip-address. So inceremnt a count variable and store 1 to the cell</li>
            </ul>
        </p>
        <p>
            <b>And this is all what we need!</b>
            <p>
                In the form of a diagram, this process will look like this:
                </br><img src="https://iili.io/J6qCOa1.png" alt="J6qCOa1.png" border="0" />
            </p>
        </p>
        <p>
            If we want to get back all unique ip's in readible format we need:
            <ul>
                <li>walk from start to end of arrays</li>
                <li>shift index bits to the left</li>
                <li>extract each bit who is '1' to address</li>
            </ul>
        </p>
        <p>
            <b>DONE!</b>
        </p>
        <p>
            But wait a minute, what about a second <i>once</i> implementation? That looks the same, but we need now 2 bits
            for store 3 state: <b>00</b> - not exist, <b>01</b> - exist once, <b>11</b> - exist more than once. So we can store now only 32 
            address into one long <i>(in theory a little bit more, around 40, and I'm thinking about it)</i>
        </p>
        <p>
            For indexing we need next:
            <ul>
                <li>we count index of long[] element from first 18bit of our int-ip</li>
                <li>next we need index for position in elements bit - we use last 4bits of our int-ip for <b>first</b> 
                    cell index, and <b>second</b> index just increase for 32</li>
                <li>
                    if stored <b>first</b> bit is 0 - and:
                    <ul>
                    <li>
                        if stored <b>second</b> bit is 0 - we got unique ip! So increment a count variable and store 1 
                        to <b>second</b> bit
                    </li>
                    <li>
                        if stored <b>second</b> bit is 1 - we got duplicated ip. Reduce a count variable and store 1 
                        to <b>first</b> bit 
                    </li>
                    </ul>
                </li>
            </ul>
        </p>
    </div>
</details>
<hr>
<h2>How it run</h2>
<p>
    I implement 2 variants of counting result. Also I do 2 variants of reading: reading by lines and classic block-read.
    Both variant show more or less equal speed, cpu and memory usage, but block reading may be more effective on 
    <b>ssd-disk</b>. Unfortunately I test it with usb3 hdd, and maximal read speed is around 110-120mb per second and
    both reader show same result.
</p>
<p>
    <i>If you it on your own ssd please let me know about result! =)</i>
</p>
<p>
    Ok, for run you need: 
</p>
<p>
    0) check, that you have <b>GIT</b>, <b>MAVEN</b> (3.9.1 and above) and <b>JAVA17</b>, or have IDE like an 
    <b>Intellij IDEA</b> where you can clone, build and run project... And also has above 512mb RAM.
</p>
<p>
    <i>If you have low RAM environment, please read <u>Java tuning</u> topic!</i>
</p>
<p>
    1) clone project to your enviroment
    <p><code>git clone https://github.com/Pavel-Tishina/ipaddreader.git</code></p>
</p>
<p>
    2) go to project directory and build
    <p><code>cd ./ipaddreader</code></p>
    <p><code>mvn clean package</code></p>
</p>
<p>
    3) run it with argument <b>-ipfile</b>
    <p><code>java -cp ./target/ipaddreader-1.0-SNAPSHOT.jar com.itgnostic.Main -ipfile='<i>../path/to/file..</i>'</code></p>
</p>
<p>
    Enjoy!
</p>
<details close>
    <summary>About an arguments</summary>
    <div>
        Here is next arguments that change mode of work, read and so on:
        <ul>
            <li>
                <code><b>-ipfile='</b><i>/path/to/your/file</i><b>'</b></code>
                <p>Only one <b>requred</b> argument, contains path to your file with <u>ip-addresses</u></p>
            </li>
            <li>
                <code><b>-mode='</b><i><b>ALL</b> or <b>ONCE</b></i><b>'</b></code>
                <p>
                    ALL - count all unique ip addresses (need more 512mb RAM) <i>[default]</i>
                </p>
                <p>
                    ONCE - count only unique ip addresses in file (need more 1024mb RAM)
                </p>
            </li>
            <li>
                <code><b>-read='</b><i><b>LINE</b> or <b>BLOCK</b></i><b>'</b></code>
                <p>
                    LINE - read file by lines, optimal speed and resources usage <i>[default]</i>
                </p>
                <p>
                    BLOCK - read file by block, performance and memory usage depends on <u>block_size</u> 
                </p>
            </li>
            <li>
                <code><b>-bs='</b><i>123456</i><b>'</b></code>
                <p>
                    Set <u>block_size</u>. You can add letter 'k', 'm', 'g' at the end for set size. <i>[default = 8mb]</i>
                </p>
            </li>
            <li>
                <code><b>-out='</b><i>/path/to/your/file</i><b>'</b></code>
                <p>
                    Set output file for save unique ip result in unpacked format like "127.0.0.1". Be careful, result 
                    can be large and you need enough space  
                </p>
            </li>
            <li>
                <code><b>-chk</b></code>
                <p>
                    This argument <u>switch on</u> checking ip addresses. <i>[default = false (switched off)]</i>
                </p>
                <p>
                    <i>Nobody told me that all ipv4 are correct! What I should do if meet some addresses like 
                    "I27.O.O.I", "777.888.999.1000","one-dot-zero-zero-one", "127.0.0" or... uph... I don't know. 
                    I believe that you a good boy and have valid addresses format, but if not - just add this.</i>
                </p>
            </li>
        </ul>
    </div>
</details>
<details close>
    <summary>Java tuning</summary>
    <div>
        <p>
            In theory you need minimum of <b>512mb</b> of RAM for count <u>all unique address</u>, and <b>1024mb</b> for 
            count <u>all addresses, that contained in file once</u>. I'm sorry, but I have prioritizes for speed and use 
            Java-core only without memory-dumping and components like database and so on...
        </p>
        <p>
            Anyway, if you want to run it with extreme-low memory environment add next argument:
            <ul>
                <li><b>-Xmx520m</b> - for mode <b>ALL</b></li>
                <li><b>-Xmx1032m</b> - for mode <b>ONCE</b></li>
            </ul>
            It use more CPU and work slower but well.
        </p>
    </div>
</details>
