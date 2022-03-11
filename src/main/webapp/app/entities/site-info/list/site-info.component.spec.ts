import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SiteInfoService } from '../service/site-info.service';

import { SiteInfoComponent } from './site-info.component';

describe('SiteInfo Management Component', () => {
  let comp: SiteInfoComponent;
  let fixture: ComponentFixture<SiteInfoComponent>;
  let service: SiteInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SiteInfoComponent],
    })
      .overrideTemplate(SiteInfoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SiteInfoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SiteInfoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.siteInfos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
