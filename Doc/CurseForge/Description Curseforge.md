<div align="center">
    <img src="https://files.catbox.moe/9hc07g.png" alt="True Sleep Banner">
</div>
<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-GPLv3-green?style=for-the-badge" alt="License">
</p>

<h1>🌙 True Sleep: The "Agency" Update (v1.3.13)</h1>

<p><strong>No Backports:</strong> I will <strong>NOT</strong> backport this mod to older versions (1.21, 1.20, etc.). Please do not ask.</p>

<p>In vanilla Minecraft, sleeping is a "cheat code" that deletes time. You right-click a bed, the screen fades to black, and the game instantly skips forward 12,000 ticks.</p>

<p><strong>Vanilla Outsider: True Sleep</strong> changes this foundation. When you sleep, the world <strong>accelerates</strong>. Instead of skipping the night, the game tick rate boosts to <strong>Quantum Speeds</strong> (variable TPS). You watch the moon zoom across the sky, stars streak by, and the sun rise rapidly.</p>

<hr>

<h2>✨ Features</h2>

<h3>🕰️ Quantum Warp (Simulation)</h3>
<p>The world doesn't pause. Furnaces continue to smelt, crops continue to grow, and copper continues to oxidize while you sleep. Everything simulates at hyper-speed.</p>

<blockquote>
    <strong>Quantum Stride Technology:</strong> We use a variable tick stride to ensure high performance.<br>
    Default Engine Speed: <strong>50 TPS</strong> (2.5x standard speed) — controls how fast everything actually moves: mobs, redstone, furnaces, the sky, all of it.<br>
    Virtual Speed: <strong>1000 TPS</strong> (50x speed) is achieved by simulating multiple ticks per server tick.
</blockquote>

<blockquote>
    <strong>Production &amp; Hopper Acceleration (v1.3.13):</strong> Smelting/brewing and hoppers coupled directly to machines are now accelerated to match the time warp speed. Redstone-locked hoppers are ignored to protect automatic sorters.<br>
    <em>Feedback Needed:</em> We need more feedback on this feature! If you find any issues with custom redstone builds or modded machines, please send an issue report for us to check. Thank you!
</blockquote>

<h3>🎞️ Visuals</h3>
<p>Watch the passage of time from your bed. No jarring "fade to black." The transition from night to day is seamless and grounded in the world.</p>
<p>Feature Showcase: <a href="https://www.youtube.com/watch?v=FcNaMSN2WG8">YouTube Link</a></p>

<h3>💤 Dreamweaver Engine</h3>
<p>Fine-tune your sleep schedule with precision:</p>
<ul>
    <li><strong>Sleep Threshold:</strong> Configure exactly when you can get into bed (dusk, midnight, etc.). Corrected to 0–23999 range.</li>
    <li><strong>Wake Time:</strong> Decide when the warp ends (dawn, noon, etc.). Now includes a full tick-to-time reference guide.</li>
    <li><strong>Hybrid Config:</strong> Use <code>/gamerule</code> for per-world settings, or <code>config/truesleep.json</code> for global defaults.</li>
</ul>

<h3>🚀 Full Agency (Uncapped)</h3>
<p>We have removed the training wheels. Engine TPS and Virtual TPS are now fully <strong>uncapped</strong>.</p>
<ul>
    <li><strong>No More Clamps:</strong> The legacy "stability clamp" that forced 50 TPS on high settings has been deleted.</li>
    <li><strong>Precision Control:</strong> Set Engine TPS to 1000? Set Virtual TPS to 100,000? You have the agency.</li>
    <li><strong>Real-Time Night:</strong> Tip: Set <strong>Engine TPS = Virtual TPS</strong> (e.g., both to 50 or 100). This sets the simulation stride to 1, meaning the night passes in <strong>true real-time</strong> at that exact tick rate with zero time dilation.</li>
</ul>

<blockquote>
    <strong>WARNING:</strong> High TPS values (Engine TPS &gt; 100) are experimental. Pushing the engine too far can cause server lag or disconnects depending on your hardware. We provide the agency; you handle the consequences.
</blockquote>

<h3>⚖️ Multiplayer</h3>
<p>One player sleeping accelerates time for <em>everyone</em> on the server.</p>
<ul>
    <li><strong>No more arguments:</strong> "1/2 players sleeping" doesn't force a skip.</li>
    <li><strong>No disruption:</strong> Other players just see the world speed up for a few seconds.</li>
</ul>

<h3>🐈 Cat Gifts</h3>
<p>We have patched the vanilla Cat logic! Normally, cats only give gifts if you sleep for 5+ seconds. True Sleep is so fast the night passes in 1 second. <strong>We fixed this:</strong> Your cats now recognize the "Time Warp" and will still grant you Morning Gifts (Phantom Membranes, Rabbit Feet, etc.).</p>

<h3>🛡️ Quantum Safety</h3>
<ul>
    <li><strong>Empty Dimensions:</strong> The mod intelligently ignores empty dimensions to prevent logic bugs.</li>
    <li><strong>Drown Immunity:</strong> Entities in water are granted biological stasis (water breathing) during the warp to prevent drowning.</li>
    <li><strong>Mob Unfreeze (Dynamic Category):</strong> A dedicated "True Sleep Mobs" GameRule category is generated, containing toggles for <em>every</em> individual entity type in the game. Performance-optimized stasis—mobs are frozen by default to save TPS. However, if you have a <strong>redstone contraption or farm</strong> that relies on a specific mob to work (e.g., an iron farm using zombies/villagers), you can selectively "unfreeze" them to keep your systems running at 1000 Virtual TPS.
        <br><br>
        <iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/FcNaMSN2WG8" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
        <br>
        <img src="https://raw.githubusercontent.com/Rifaditya/Vanilla-Outsider-True-Sleep/master/Images/2026-02-22_11.17.09.png" alt="Mob Unfreeze Category">
    </li>
    <li><strong>Golden Dandelion:</strong> Compatible with age-locked mobs (from other mods).</li>
</ul>

<hr>

<h2>⚙️ Config</h2>
<p>The mod works out of the box with zero setup.</p>
<ul>
    <li><strong>Global Template:</strong> <code>config/truesleep.json</code> (Sets defaults for new worlds)</li>
    <li><strong>In-Game:</strong> Use <code>/gamerule truesleep:</code> for core settings and the <strong>True Sleep Mobs</strong> category for entity control.
        <ul>
            <li><code>truesleep:engine_tps</code> — <strong>Performance Limit</strong>: How hard the server works during sleep (Default: 50)</li>
            <li><code>truesleep:virtual_tps</code> — <strong>Time Speed</strong>: How fast the night flies by (Default: 1000)</li>
            <li><code>truesleep:sleep_threshold</code> — <strong>Sleep Threshold</strong>: When players can start sleeping (Default: 12542)</li>
            <li><code>truesleep:wake_time</code> — <strong>Wake Time</strong>: What time players wake up (Default: 0 / Sunrise)</li>
            <li><code>truesleep:accelerate_machines</code> — <strong>Accelerate Machines</strong>: Speeds up furnaces/brewers during sleep (Default: ON)</li>
            <li><code>truesleep:accelerate_hoppers</code> — <strong>Accelerate Hoppers</strong>: Speeds up hoppers coupled to machines (Default: ON)</li>
            <li><code>truesleep:freeze_mobs</code> — <strong>Freeze Mobs</strong>: Pauses all mobs during sleep (Default: ON)</li>
            <li><code>truesleep:freeze_workers</code> — <strong>Freeze Villagers</strong>: Also pauses villagers and iron golems (Default: OFF)</li>
            <li><code>truesleep:drown_immunity</code> — <strong>Drown Immunity</strong>: Prevents drowning during sleep (Default: ON)</li>
        </ul>
    </li>
    <li><strong>ModMenu / Cloth Config:</strong> All settings above are also available through the optional ModMenu config GUI (requires <a href="https://modrinth.com/mod/cloth-config">Cloth Config</a> and <a href="https://modrinth.com/mod/modmenu">ModMenu</a>).</li>
</ul>

<blockquote>
    <strong>Recommended Mod:</strong> Since this mod generates 150+ GameRules, it is highly recommended to use <strong><a href="https://modrinth.com/mod/collapsible-gamerules">Collapsible Game Rules</a></strong> for a cleaner UI.
</blockquote>

<hr>

<h2>🧩 Suggested Mods</h2>
<p>For the best experience, we recommend installing:</p>
<ul>
    <li><strong><a href="https://modrinth.com/mod/collapsible-gamerules">Collapsible Game Rules</a></strong>: Prevents the GameRules menu from becoming cluttered by grouping the 150+ new mob toggles into a clean, searchable category.</li>
</ul>

<hr>

<h2>📦 Install</h2>
<ol>
    <li>Install <strong><a href="https://modrinth.com/mod/fabric-api">Fabric API</a></strong>.</li>
    <li>Download <code>vanilla-outsider-true-sleep.jar</code> and place it in your <code>mods</code> folder.</li>
</ol>

<hr>

<h2>🧩 Compatibility</h2>
<table border="1" cellpadding="5">
    <thead>
        <tr>
            <th>Feature</th>
            <th>Fabric (26.1+)</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Singleplayer</td>
            <td>✅</td>
        </tr>
        <tr>
            <td>Multiplayer (LAN/Server)</td>
            <td>✅</td>
        </tr>
        <tr>
            <td><strong>VO: Better Dogs</strong></td>
            <td>✅ (Wolves cool down faster!)</td>
        </tr>
        <tr>
            <td><strong>Create Mod</strong></td>
            <td>✅ (Kinetic networks stay at physical speed)</td>
        </tr>
        <tr>
            <td><strong>Agrarian Reform</strong></td>
            <td>✅ (Offline growth catch-up compatible)</td>
        </tr>
        <tr>
            <td>Empty Dimensions</td>
            <td>✅</td>
        </tr>
    </tbody>
</table>

<hr>

<h2>☕ Support</h2>
<p>If you enjoy <strong>True Sleep</strong> and the <strong>Vanilla Outsider</strong> philosophy, consider fueling the next update with a coffee!</p>
<p>
    <a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Ko-fi"></a>
    <a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
    <a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

<blockquote>
    <strong>Indonesian Users:</strong> SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!
</blockquote>

<hr>

<h2>📜 Credits</h2>
<table border="1" cellpadding="5">
    <thead>
        <tr>
            <th>Role</th>
            <th>Author</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><strong>Creator</strong></td>
            <td>DasikIgaijin</td>
        </tr>
        <tr>
            <td><strong>Collection</strong></td>
            <td>Vanilla Outsider</td>
        </tr>
        <tr>
            <td><strong>License</strong></td>
            <td>GNU GPLv3</td>
        </tr>
    </tbody>
</table>

<hr>

<blockquote>
    <strong>IMPORTANT:</strong> This mod is part of the <strong>Vanilla Outsider</strong> collection. You are free to use it in modpacks, videos, and servers.
    <blockquote>
        <strong>Modpack Permissions:</strong> You are free to include this mod in modpacks, <strong>provided the modpack is hosted on the same platform</strong> (e.g. Modrinth).<br><br>
        <strong>Cross-platform distribution is not permitted.</strong> If you download this mod from Modrinth, your modpack must also be published on Modrinth.
    </blockquote>
</blockquote>

<hr>

<div align="center">
    <p><strong>Made with ❤️ for the Minecraft community</strong></p>
    <p><em>Part of the Vanilla Outsider Collection</em></p>
</div>
