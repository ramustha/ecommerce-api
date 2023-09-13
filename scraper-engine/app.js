const express = require('express')
const puppeteer = require('puppeteer-extra')
const {chromium} = require('playwright-extra')
const StealthPlugin = require("puppeteer-extra-plugin-stealth")
const app = express()
const port = 3000
const defaultTimeout = 0

async function performScrapingPuppeteer(url, timeout, res, stealth) {
    if (stealth === '1') {
        puppeteer.use(StealthPlugin());
        console.log('stealth mode..' + url);
    }

    const browser = await puppeteer.launch({
        headless: 'new',
        defaultViewport: null,
    });
    const page = await browser.newPage();

    try {
        console.log('Scraping puppeteer..' + url);
        await page.goto(url, {waitUntil: 'networkidle0'});

        // await page.click("div[id=main]")
        // await page.locator("input[class=shopee-searchbar-input__input]").fill("batocera")
        // await page.keyboard.press('Enter');
        await page.waitForTimeout(timeout)

        let screenshotBuffer = await page.screenshot({fullPage: true});
        console.log('All done ✨');

        await browser.close();

        // Set the correct content-type.
        res.setHeader('Content-Type', 'image/png');
        res.send(screenshotBuffer);
    } catch (err) {
        await browser.close();
        console.error(err);
        res.status(500).send('An error occurred while trying to scrape the website.');
    }
}

async function performScrapingPlaywright(url, timeout, res, stealth) {
    if (stealth === '1') {
        chromium.use(StealthPlugin());
        console.log('stealth mode..' + url);
    }

    const browser = await chromium.launchPersistentContext("/", {headless: true});
    const page = await browser.newPage()

    try {
        console.log('Scraping playwright..' + url);
        await page.goto(url, {waitUntil: 'networkidle'})

        // await page.click("div[id=main]")
        // await page.locator("input[class=shopee-searchbar-input__input]").fill("batocera")
        // await page.keyboard.press('Enter');
        await page.waitForTimeout(timeout)

        let screenshotBuffer = await page.screenshot({fullPage: true});
        console.log('All done ✨');

        await browser.close();

        // Set the correct content-type.
        res.setHeader('Content-Type', 'image/png');
        res.send(screenshotBuffer);
    } catch (err) {
        await browser.close();
        console.error(err);
        res.status(500).send('An error occurred while trying to scrape the website.');
    }
}

app.get('/', async (req, res) => {
    let {url, timeout, mode, stealth} = req.query;

    if (!url) {
        return res.status(400).send('The url parameter is required.');
    }

    timeout = timeout ? parseInt(timeout) : defaultTimeout;
    if (mode === '1')
        await performScrapingPuppeteer(url, timeout, res, stealth);
    else
        await performScrapingPlaywright(url, timeout, res, stealth);
});

app.listen(port, () => {
    console.log(`Server running on http://localhost:${port}`);
});
